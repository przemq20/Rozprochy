//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.3
//
// <auto-generated>
//
// Generated from file `office.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package Office;

public class UserAlreadyExists extends com.zeroc.Ice.UserException
{
    public UserAlreadyExists()
    {
    }

    public UserAlreadyExists(Throwable cause)
    {
        super(cause);
    }

    public String ice_id()
    {
        return "::Office::UserAlreadyExists";
    }

    /** @hidden */
    @Override
    protected void _writeImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice("::Office::UserAlreadyExists", -1, true);
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _readImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        istr_.endSlice();
    }

    /** @hidden */
    public static final long serialVersionUID = 848697964162894032L;
}