package org.succlz123.htmlview.span;

import android.text.style.ClickableSpan;

/**
 * This span defines what should happen if a table is clicked. This abstract class is defined so
 * that applications can access the raw table HTML and do whatever they'd like to render it (e.g.
 * show it in a WebView).
 */
public abstract class ClickableTableSpan extends ClickableSpan {
    protected String tableHtml;

    // This sucks, but we need this so that each table can get its own ClickableTableSpan.
    // Otherwise, we end up removing the clicking from earlier tables.
    public abstract ClickableTableSpan newInstance();

    public void setTableHtml(String tableHtml) {
        this.tableHtml = tableHtml;
    }

    public String getTableHtml() {
        return tableHtml;
    }
}
