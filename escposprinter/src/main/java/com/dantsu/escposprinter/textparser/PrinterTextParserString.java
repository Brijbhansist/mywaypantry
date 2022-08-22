package com.dantsu.escposprinter.textparser;

import java.util.Arrays;

import com.dantsu.escposprinter.EscPosPrinterCommands;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;

public class PrinterTextParserString implements IPrinterTextParserElement {
    private final String text;
    private final byte[] textSize;
    private final byte[] textBold;
    private final byte[] textUnderline;
    
    public PrinterTextParserString(String text, byte[] textSize, byte[] textBold, byte[] textUnderline) {
        this.text = text;
        this.textSize = textSize;
        this.textBold = textBold;
        this.textUnderline = textUnderline;
    }

    @Override
    public int length() {
        int coef = 1;
        
        if (Arrays.equals(this.textSize, EscPosPrinterCommands.TEXT_SIZE_DOUBLE_WIDTH) || Arrays.equals(this.textSize, EscPosPrinterCommands.TEXT_SIZE_BIG)) {
            coef = 2;
        }
        
        return this.text.length() * coef;
    }

    /**
     * Print text
     *
     * @param printerSocket Instance of EscPosPrinterCommands
     * @return this Fluent method
     */
    @Override
    public PrinterTextParserString print(EscPosPrinterCommands printerSocket) throws EscPosEncodingException {
        printerSocket.printText(this.text, this.textSize, this.textBold, this.textUnderline);
        return this;
    }
}
