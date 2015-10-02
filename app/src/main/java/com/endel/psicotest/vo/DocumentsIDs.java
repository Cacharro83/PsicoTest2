package com.endel.psicotest.vo;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;
import java.util.Vector;

public class DocumentsIDs extends Vector<RespuestasUsuarioNM_VO> implements KvmSerializable {
    private static final long serialVersionUID = -1166006770093411055L;

    @Override
    public Object getProperty(int arg0) {
        return this.get(arg0);
    }

    @Override
    public int getPropertyCount() {
        return this.size();
    }

    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
        arg2.name = "string";
        arg2.type = PropertyInfo.STRING_CLASS;
    }

    @Override
    public void setProperty(int arg0, Object arg1) {
        this.add((RespuestasUsuarioNM_VO)arg1);
    }

}