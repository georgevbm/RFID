package br.com.george.apprfid.RFID.DotR900;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import br.com.george.apprfid.Activity.ListarEtiquetaActivity;
import br.com.george.apprfid.RFID.ILeitor;
import br.com.george.apprfid.RFID.Leitor;

public class R900 extends Leitor implements ILeitor {
    public static final int MSG_REFRESH_LIST_TAG = 22;
    protected String mLastCmd;
    protected boolean mSingleTag;
    protected boolean mUseMask;
    protected int mTimeout;
    protected boolean mQuerySelected;
    private String mStrAccessErrMsg;

    public R900(Activity activity, Handler handler, OnBtEventListener listener) {
        super(activity, handler, listener);
    }

    public void leitura() {
        while (true) {
            final String parameter = mPacketParser.popPacket();

            if (mConnected == false)
                break;

            if (parameter != null) {
                processPacket(parameter);
            } else
                break;
        }

        mHandler.sendEmptyMessage(MSG_REFRESH_LIST_TAG);
    }

    public void sendCmdOpenInterface1() {
        sendData(R900Protocol.OPEN_INTERFACE_1);
    }

    public void sendSettingTxCycle(int on, int off) {
        sendData(R900Protocol.makeProtocol(R900Protocol.CMD_SET_TX_CYCLE, new int[]{on, off}));
    }

    public void sendData(byte[] bytes) {
        if (mConnected == true) {
            mConnectedThread.write(bytes);
        }
    }

    public void sendCmdInventory() {
        sendCmdInventory(mSingleTag ? 1 : 0, mUseMask ? (mQuerySelected ? 3 : 2) : 0, mTimeout);
    }

    public void sendCmdInventory(int f_s, int f_m, int to) {
        R900Status.setOperationMode(1);    // eric 2012.11.29
        mLastCmd = R900Protocol.CMD_INVENT;
        sendData(R900Protocol.makeProtocol(mLastCmd, new int[]{f_s, f_m, to}));
    }

    private synchronized void processPacket(final String param) {
        if (param == null || param.length() <= 0)
            return;

        if (mConnected == false)
            return;

        final String CMD = param.toLowerCase();

        if (CMD.indexOf("^") == 0 || CMD.indexOf("$") == 0
                || CMD.indexOf("ok") == 0 || CMD.indexOf("err") == 0
                || CMD.indexOf("end") == 0) {

            if ((CMD.indexOf("ok,") != -1)) {
                final int idxComma = CMD.indexOf(",");
                final String mStrBattLevel = CMD.substring(idxComma + 1, CMD.length());

                R900Status.setBatteryLevel(Integer.parseInt(mStrBattLevel));

            } else if (CMD.indexOf("$trigger=1") == 0) {
                setupOperationParameter();
                sendCmdInventory();

            } else if (CMD.indexOf("$trigger=0") == 0) {
                sendCmdStop();
            }

            if (CMD.indexOf("ok") == 0)
                mStrAccessErrMsg = null;
        } else {
            if (mLastCmd.equalsIgnoreCase(R900Protocol.CMD_INVENT))
                atualizarListaTag(param);
        }
    }

    public void sendInventParam(int session, int q, int m_ab) {
        sendData(R900Protocol.makeProtocol(R900Protocol.CMD_INVENT_PARAM, new int[]{session, q, m_ab}));
    }

    public void setupOperationParameter() {
        sendInventParam(1, 5, 0);
        final ListarEtiquetaActivity.SelectMask selMask = ListarEtiquetaActivity.getSelectMask();
        boolean bUseMask = false;
        boolean bQuerySelected = (selMask.Bits > 0);

        int timeout = 0;
        final String strTimeout = "0";
        if (strTimeout != null && strTimeout.length() > 0) {
            timeout = Integer.parseInt(strTimeout);
            if (timeout < 0)
                timeout = 0;
            timeout *= 1000;
        }

        if (ListarEtiquetaActivity.UseMask == true && selMask.Bits > 0) {
            if (ListarEtiquetaActivity.Type == 0) {
                if (selMask.Pattern != null && selMask.Pattern.length() != 0) {
                    sendSetSelectAction(selMask.Bits, selMask.Bank, selMask.Offset, selMask.Pattern, 0);
                    bUseMask = true;
                }
            } else {
                if (selMask.Bank != 4) {
                    sendSetSelectAction(selMask.Bits, selMask.Bank, selMask.Offset, selMask.Pattern, 0);
                    bUseMask = true;
                }
            }
        }

        //setOpMode(mChkSingleTag.isChecked(), bUseMask, timeout, bQuerySelected);
    }

    public void sendSetSelectAction(int bits, int mem, int b_offset, String pattern, int action) {
        sendData(R900Protocol.makeProtocol(R900Protocol.CMD_SEL_MASK, new int[]{0, bits, mem, b_offset}, pattern, new int[]{R900Protocol.SKIP_PARAM, action}));
    }

    public void sendCmdStop() {
        R900Status.setOperationMode(0);
        mLastCmd = R900Protocol.CMD_STOP;
        sendData(R900Protocol.makeProtocol(mLastCmd, (int[]) null));
    }

    private void pararComunicacaoLeitor() {
        sendData(R900Protocol.BYE);
    }

    public void finalize() {
        try {
            pararComunicacaoLeitor();
            super.finalize();
        }
        catch (Exception ex) {
            Log.d("ERRO", ex.getMessage());
        }

    }

}
