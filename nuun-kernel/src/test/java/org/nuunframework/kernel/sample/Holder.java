package org.nuunframework.kernel.sample;

import org.nuunframework.kernel.plugins.configuration.NuunProperty;
import org.nuunframework.kernel.plugins.logs.NuunLog;
import org.slf4j.Logger;


public class Holder
{

    @NuunLog
    private Logger myLogger;

    @AnnoCustomLog
    private Logger myLogger2;

    public Holder()
    {
    }

    @NuunProperty("user.name")
//    @AnnoCustomProperty("user.name")
    public String   user = null;

    @NuunProperty("user.id")
    public Long      id;

    @NuunProperty("user.password")
    public String    password;

    @NuunProperty("test.int")
    public int       _int;
    @NuunProperty("test.int")
    public Integer   _Integer;
    @NuunProperty("test.int")
    public String    __Integer;

    @NuunProperty("test.long")
    public long      _long;
    @NuunProperty("test.long")
    public Long      _Long;
    @NuunProperty("test.long")
    public String    __Long;

    @NuunProperty("test.boolean.true")
    public boolean   _booleanTrue;
    @NuunProperty("test.boolean.true")
    public Boolean   _BooleanTrue;
    @NuunProperty("test.boolean.true")
    public String    __BooleanTrue;

    @NuunProperty("test.boolean.false")
    public boolean   _booleanFalse;
    @NuunProperty("test.boolean.false")
    public Boolean   _BooleanFalse;
    @NuunProperty("test.boolean.false")
    public String    __BooleanFalse;

    @NuunProperty("test.short")
    public short     _short;
    @NuunProperty("test.short")
    public Short     _Short;
    @NuunProperty("test.short")
    public String    __Short;

    @NuunProperty("test.byte")
    public byte      _byte;
    @NuunProperty("test.byte")
    public Byte      _Byte;
    @NuunProperty("test.byte")
    public String    __Byte;

    @NuunProperty("test.float")
    public float     _float;
    @NuunProperty("test.float")
    public Float     _Float;
    @NuunProperty("test.float")
    public String    __Float;

    @NuunProperty("test.double")
    public double    _double;
    @NuunProperty("test.double")
    public Double    _Double;
    @NuunProperty("test.double")
    public String    __Double;

    @NuunProperty("test.char")
    public char      _char;
    @NuunProperty("test.char")
    public Character _Character;
    @NuunProperty("test.char")
    public String    __Character;
    @NuunProperty("test.no")
    public String    __nothing;

    public Logger getLogger()
    {
        return myLogger;
    }

    public Logger getLogger2()
    {
        return myLogger2;
    }
}
