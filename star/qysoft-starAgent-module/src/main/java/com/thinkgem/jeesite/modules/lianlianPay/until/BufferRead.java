package com.thinkgem.jeesite.modules.lianlianPay.until;

import java.io.IOException;
import java.io.InputStream;

/**
 * 流读取
 * @author shmily
 * @date Mar 16, 2011 4:50:14 PM
 */
public class BufferRead{

    public synchronized String readBuffer(InputStream in) throws IOException
    {
        String sReturnBuf = "";
        int c;
        byte lengthbuf[] = new byte[Globals.PACK_HEAD_LENGTH];
        int pos = 0;
        // 先取长度
        for (int j = 0; j < Globals.PACK_HEAD_LENGTH; j++)
        {
            c = in.read();
            lengthbuf[pos++] = (byte) c;
        }
        String slength = new String(lengthbuf);
        if (DataTypeUtil.isInt(slength) && slength.trim().length() > 0)
        {
            int bodylen = new Integer(slength).intValue()
                    + Globals.TRANS_RETURN_SUCCESS_LENGTH;
            byte bodybuf[] = new byte[bodylen];
            int pos1 = 0;
            for (int k = 0; k < bodylen; k++)
            {
                c = in.read();
                bodybuf[pos1++] = (byte) c;
            }
            sReturnBuf = new String(bodybuf);
        }
        return sReturnBuf;
    }

    /*
     * 读xml的buf
     */

    public synchronized String readXmlBuffer(InputStream in) throws IOException
    {
        String sReturnBuf = "";
        int c;
        byte lengthbuf[] = new byte[Globals.PACK_HEAD_LENGTH];
        int pos = 0;
        // 先取长度
        for (int j = 0; j < Globals.PACK_HEAD_LENGTH; j++)
        {
            c = in.read();
            lengthbuf[pos++] = (byte) c;
        }
        String slength = new String(lengthbuf);
        if (DataTypeUtil.isInt(slength) && slength.trim().length() > 0)
        {
            int bodylen = new Integer(slength).intValue();
            byte bodybuf[] = new byte[bodylen];
            int pos1 = 0;
            for (int k = 0; k < bodylen; k++)
            {
                c = in.read();
                bodybuf[pos1++] = (byte) c;
            }
            sReturnBuf = new String(bodybuf);
        }
        return sReturnBuf;
    }

}
