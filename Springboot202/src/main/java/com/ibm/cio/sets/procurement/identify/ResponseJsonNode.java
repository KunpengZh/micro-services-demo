package com.ibm.cio.sets.procurement.identify;

public enum ResponseJsonNode {
    DATA("data"), ATTRIBUTES("attributes"), ERRORS("errors"), TITLE("title"), ID("id"), LINKS(
            "links"), SELF("self"), RELATED("related"), STATUS("status"), CODE("code"), DETAIL(
            "detail"), SOURCE("source"), POINTER("pointer"), PARAMETER(
            "parameter"), META("meta"), JSONAPI("jsonapi"), VERSION("version");
    private String node;

    ResponseJsonNode(String node) {
        this.node = node;
    }

    public String getNodeName() {
        return node;
    }
}
