package br.com.george.apprfid.Activity;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.george.apprfid.Adapter.TagAdapter;
import br.com.george.apprfid.R;
import br.com.george.apprfid.RFID.DotR900.OnBtEventListener;
import br.com.george.apprfid.RFID.DotR900.R900;

public class ListarEtiquetaActivity extends AppCompatActivity implements OnBtEventListener {
    //region VARIAVEIS
    private R900 leitor;
    public static final int MSG_ENABLE_LINK_CTRL = 10;
    public static final int MSG_DISABLE_LINK_CTRL = 11;
    public static final int MSG_ENABLE_DISCONNECT = 12;
    public static final int MSG_DISABLE_DISCONNECT = 13;
    public static final int MSG_SHOW_TOAST = 20;
    public static final int MSG_REFRESH_LIST_TAG = 22;
    public static final int MSG_BT_DATA_RECV = 10;
    private static final int[] TX_DUTY_OFF =
            {10, 40, 80, 100, 160, 180};

    private static final int[] TX_DUTY_ON =
            {190, 160, 70, 40, 20};

    private static final String[] TXT_DUTY =
            {"90%", "80%", "60%", "41%", "20%"};

    public static int Type;

    public static class SelectMask {
        public int Bank;
        public int Offset;
        public int Bits;
        public String Pattern;
        public String TagId;
    }

    public static SelectMask SelMask = new SelectMask();
    public static boolean UseMask = false;
    private TextView lblTotalTags;
    private ListView lstTag;
    private BaseAdapter mAdapterTag;
    private Toolbar mToolbar;
    private String tag;
    private List<String> tags;
    //endregion

    //region HANDLER
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_BT_DATA_RECV:
                    onNotifyBtDataRecv();
                    break;
                case MSG_SHOW_TOAST:
                    Toast.makeText(ListarEtiquetaActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case MSG_REFRESH_LIST_TAG:
                    try {
                        mAdapterTag.notifyDataSetChanged();

                        lblTotalTags.setText(String.valueOf(leitor.getListaTags().size()));
                    } catch (Exception ex) {
                        Log.d("ERRO", ex.getMessage());
                    }

            }
        }
    };
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_etiqueta);

        if (getIntent().hasExtra("addressDispositivo")) {
            leitor = new R900(ListarEtiquetaActivity.this, mHandler, this);

            String addressDispositivo = getIntent().getExtras().getString("addressDispositivo");
            leitor.conectar(addressDispositivo);

            lblTotalTags = (TextView) findViewById(R.id.lblTotalTags);
            lblTotalTags.setText("0");
            lstTag = (ListView) findViewById(R.id.lstEtiquetas);
            tags = new ArrayList<>();

            tags = leitor.getListaTags();
            mAdapterTag = new TagAdapter(getApplicationContext(), tags);
            lstTag.setAdapter(mAdapterTag);

            lstTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ListarEtiquetaActivity.this, CadPatrimonioActivity.class);
                    tag = leitor.getListaTags().get(position);
                    abrirMenu();
                }
            });
            //region Toolbar
            mToolbar = (Toolbar) findViewById(R.id.ToolbarTop);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            mToolbar.findViewById(R.id.btnVoltar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            //endregion
        }

        onNotifyBtDataRecv();
    }


    //region EVENTOS DA TELA
    @Override
    public void onNotifyBtDataRecv() {
        if (leitor == null)
            return;

        try {
            leitor.leitura();
        }
        catch (Exception ex) {
            Log.d("ERRO", ex.getMessage());
        }

    }

    @Override
    public void onBtConnected(BluetoothDevice device) {
        setEnabledLinkCtrl(true);

        showToastByOtherThread("Conectou: " + leitor.getDispositivo().getName(), Toast.LENGTH_SHORT);
        leitor.sendCmdOpenInterface1();

        leitor.sendSettingTxCycle(TX_DUTY_ON[0], TX_DUTY_OFF[0]);
    }
    //endregion

    //region MENSAGENS DO HANDLER

    private void setEnabledLinkCtrl(boolean bEnable) {
        if (bEnable)
            mHandler.sendEmptyMessageDelayed(MSG_ENABLE_LINK_CTRL, 50);
        else
            mHandler.sendEmptyMessageDelayed(MSG_DISABLE_LINK_CTRL, 50);
    }

    private void showToastByOtherThread(String msg, int time) {
        mHandler.removeMessages(MSG_SHOW_TOAST);
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SHOW_TOAST, time, 0, msg));
    }
    //endregion

    //region MÉTODOS DA TELA
    public void onDestroy() {
        super.onDestroy();

        leitor.finalize();
    }

    public static SelectMask getSelectMask() {
        SelectMask selMask = new SelectMask();

        if (Type == 0) {
            selMask.Bank = 1;//0;
            selMask.Offset = 16;//SelMask.Offset;

            final String pattern = selMask.Pattern = SelMask.TagId;
            if (pattern != null) {
                final int LEN = pattern.length();
                selMask.Bits = LEN * 4;
            } else {
                selMask.Bits = 0;
                selMask.Pattern = null;
            }
        } else {
            if (SelMask.Bank == 4/*0*/) {
                selMask.Bits = 0;
            } else {
                selMask.Bank = SelMask.Bank;
                selMask.Offset = SelMask.Offset;

                final String pattern = selMask.Pattern = SelMask.Pattern;
                if (pattern != null) {
                    final int LEN = pattern.length();
                    selMask.Bits = LEN * 4;
                } else {
                    selMask.Bits = 0;
                    selMask.Pattern = null;
                }
            }
        }
        return selMask;
    }
    //endregione

    //region Métodos não utilizados
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_listar_etiqueta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    //endregion

    public void abrirMenu() {
        final Dialog dialog = new Dialog(ListarEtiquetaActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu_etiqueta);

        Button btnNovo = (Button) dialog.findViewById(R.id.btnNovaEtiqueta);
        Button btnCancelar = (Button) dialog.findViewById(R.id.btnCancelarPatrimonio);

        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListarEtiquetaActivity.this, CadPatrimonioActivity.class);
                intent.putExtra("tag", tag);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}