package com.nehad.assettrackingapp.Model;

public class TableHeader {
    int id;
    String headerName ;

    public TableHeader(int id, String headerName) {
        this.id = id;
        this.headerName = headerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }
}
