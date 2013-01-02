package org.nuunframework.kernel.sample;

import org.nuunframework.kernel.plugins.configuration.Property;
import org.nuunframework.kernel.plugins.logs.Log;
import org.slf4j.Logger;


public class Holder
{

    @Log
    private Logger myLogger;

    public Holder()
    {
    }

    @Property("user.name")
    public String   user = null;

    @Property("user.id")
    public Long      id;

    @Property("user.password")
    public String    password;

    @Property("test.int")
    public int       _int;
    @Property("test.int")
    public Integer   _Integer;
    @Property("test.int")
    public String    __Integer;

    @Property("test.long")
    public long      _long;
    @Property("test.long")
    public Long      _Long;
    @Property("test.long")
    public String    __Long;

    @Property("test.boolean.true")
    public boolean   _booleanTrue;
    @Property("test.boolean.true")
    public Boolean   _BooleanTrue;
    @Property("test.boolean.true")
    public String    __BooleanTrue;

    @Property("test.boolean.false")
    public boolean   _booleanFalse;
    @Property("test.boolean.false")
    public Boolean   _BooleanFalse;
    @Property("test.boolean.false")
    public String    __BooleanFalse;

    @Property("test.short")
    public short     _short;
    @Property("test.short")
    public Short     _Short;
    @Property("test.short")
    public String    __Short;

    @Property("test.byte")
    public byte      _byte;
    @Property("test.byte")
    public Byte      _Byte;
    @Property("test.byte")
    public String    __Byte;

    @Property("test.float")
    public float     _float;
    @Property("test.float")
    public Float     _Float;
    @Property("test.float")
    public String    __Float;

    @Property("test.double")
    public double    _double;
    @Property("test.double")
    public Double    _Double;
    @Property("test.double")
    public String    __Double;

    @Property("test.char")
    public char      _char;
    @Property("test.char")
    public Character _Character;
    @Property("test.char")
    public String    __Character;
    @Property("test.no")
    public String    __nothing;

    public Logger getLogger()
    {
        return myLogger;
    }
}
