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

public interface AccountsManagerPrx extends com.zeroc.Ice.ObjectPrx
{
    default Account register(String name, CallbackReceiverPrx callbackReceiver)
        throws UserAlreadyExists
    {
        return register(name, callbackReceiver, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default Account register(String name, CallbackReceiverPrx callbackReceiver, java.util.Map<String, String> context)
        throws UserAlreadyExists
    {
        try
        {
            return _iceI_registerAsync(name, callbackReceiver, context, true).waitForResponseOrUserEx();
        }
        catch(UserAlreadyExists ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<Account> registerAsync(String name, CallbackReceiverPrx callbackReceiver)
    {
        return _iceI_registerAsync(name, callbackReceiver, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Account> registerAsync(String name, CallbackReceiverPrx callbackReceiver, java.util.Map<String, String> context)
    {
        return _iceI_registerAsync(name, callbackReceiver, context, false);
    }

    /**
     * @hidden
     * @param iceP_name -
     * @param iceP_callbackReceiver -
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Account> _iceI_registerAsync(String iceP_name, CallbackReceiverPrx iceP_callbackReceiver, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Account> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "register", null, sync, _iceE_register);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeString(iceP_name);
                     ostr.writeProxy(iceP_callbackReceiver);
                 }, istr -> {
                     Account ret;
                     ret = Account.ice_read(istr);
                     return ret;
                 });
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_register =
    {
        UserAlreadyExists.class
    };

    default Account login(String name, CallbackReceiverPrx callbackReceiver)
        throws UserNotExists
    {
        return login(name, callbackReceiver, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default Account login(String name, CallbackReceiverPrx callbackReceiver, java.util.Map<String, String> context)
        throws UserNotExists
    {
        try
        {
            return _iceI_loginAsync(name, callbackReceiver, context, true).waitForResponseOrUserEx();
        }
        catch(UserNotExists ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<Account> loginAsync(String name, CallbackReceiverPrx callbackReceiver)
    {
        return _iceI_loginAsync(name, callbackReceiver, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Account> loginAsync(String name, CallbackReceiverPrx callbackReceiver, java.util.Map<String, String> context)
    {
        return _iceI_loginAsync(name, callbackReceiver, context, false);
    }

    /**
     * @hidden
     * @param iceP_name -
     * @param iceP_callbackReceiver -
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Account> _iceI_loginAsync(String iceP_name, CallbackReceiverPrx iceP_callbackReceiver, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Account> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "login", null, sync, _iceE_login);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeString(iceP_name);
                     ostr.writeProxy(iceP_callbackReceiver);
                 }, istr -> {
                     Account ret;
                     ret = Account.ice_read(istr);
                     return ret;
                 });
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_login =
    {
        UserNotExists.class
    };

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static AccountsManagerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, ice_staticId(), AccountsManagerPrx.class, _AccountsManagerPrxI.class);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static AccountsManagerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, context, ice_staticId(), AccountsManagerPrx.class, _AccountsManagerPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static AccountsManagerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, ice_staticId(), AccountsManagerPrx.class, _AccountsManagerPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static AccountsManagerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, context, ice_staticId(), AccountsManagerPrx.class, _AccountsManagerPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @return A proxy for this type.
     **/
    static AccountsManagerPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, AccountsManagerPrx.class, _AccountsManagerPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type.
     **/
    static AccountsManagerPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, facet, AccountsManagerPrx.class, _AccountsManagerPrxI.class);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the per-proxy context.
     * @param newContext The context for the new proxy.
     * @return A proxy with the specified per-proxy context.
     **/
    @Override
    default AccountsManagerPrx ice_context(java.util.Map<String, String> newContext)
    {
        return (AccountsManagerPrx)_ice_context(newContext);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the adapter ID.
     * @param newAdapterId The adapter ID for the new proxy.
     * @return A proxy with the specified adapter ID.
     **/
    @Override
    default AccountsManagerPrx ice_adapterId(String newAdapterId)
    {
        return (AccountsManagerPrx)_ice_adapterId(newAdapterId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoints.
     * @param newEndpoints The endpoints for the new proxy.
     * @return A proxy with the specified endpoints.
     **/
    @Override
    default AccountsManagerPrx ice_endpoints(com.zeroc.Ice.Endpoint[] newEndpoints)
    {
        return (AccountsManagerPrx)_ice_endpoints(newEndpoints);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator cache timeout.
     * @param newTimeout The new locator cache timeout (in seconds).
     * @return A proxy with the specified locator cache timeout.
     **/
    @Override
    default AccountsManagerPrx ice_locatorCacheTimeout(int newTimeout)
    {
        return (AccountsManagerPrx)_ice_locatorCacheTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the invocation timeout.
     * @param newTimeout The new invocation timeout (in seconds).
     * @return A proxy with the specified invocation timeout.
     **/
    @Override
    default AccountsManagerPrx ice_invocationTimeout(int newTimeout)
    {
        return (AccountsManagerPrx)_ice_invocationTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for connection caching.
     * @param newCache <code>true</code> if the new proxy should cache connections; <code>false</code> otherwise.
     * @return A proxy with the specified caching policy.
     **/
    @Override
    default AccountsManagerPrx ice_connectionCached(boolean newCache)
    {
        return (AccountsManagerPrx)_ice_connectionCached(newCache);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoint selection policy.
     * @param newType The new endpoint selection policy.
     * @return A proxy with the specified endpoint selection policy.
     **/
    @Override
    default AccountsManagerPrx ice_endpointSelection(com.zeroc.Ice.EndpointSelectionType newType)
    {
        return (AccountsManagerPrx)_ice_endpointSelection(newType);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for how it selects endpoints.
     * @param b If <code>b</code> is <code>true</code>, only endpoints that use a secure transport are
     * used by the new proxy. If <code>b</code> is false, the returned proxy uses both secure and
     * insecure endpoints.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default AccountsManagerPrx ice_secure(boolean b)
    {
        return (AccountsManagerPrx)_ice_secure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the encoding used to marshal parameters.
     * @param e The encoding version to use to marshal request parameters.
     * @return A proxy with the specified encoding version.
     **/
    @Override
    default AccountsManagerPrx ice_encodingVersion(com.zeroc.Ice.EncodingVersion e)
    {
        return (AccountsManagerPrx)_ice_encodingVersion(e);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its endpoint selection policy.
     * @param b If <code>b</code> is <code>true</code>, the new proxy will use secure endpoints for invocations
     * and only use insecure endpoints if an invocation cannot be made via secure endpoints. If <code>b</code> is
     * <code>false</code>, the proxy prefers insecure endpoints to secure ones.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default AccountsManagerPrx ice_preferSecure(boolean b)
    {
        return (AccountsManagerPrx)_ice_preferSecure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the router.
     * @param router The router for the new proxy.
     * @return A proxy with the specified router.
     **/
    @Override
    default AccountsManagerPrx ice_router(com.zeroc.Ice.RouterPrx router)
    {
        return (AccountsManagerPrx)_ice_router(router);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator.
     * @param locator The locator for the new proxy.
     * @return A proxy with the specified locator.
     **/
    @Override
    default AccountsManagerPrx ice_locator(com.zeroc.Ice.LocatorPrx locator)
    {
        return (AccountsManagerPrx)_ice_locator(locator);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for collocation optimization.
     * @param b <code>true</code> if the new proxy enables collocation optimization; <code>false</code> otherwise.
     * @return A proxy with the specified collocation optimization.
     **/
    @Override
    default AccountsManagerPrx ice_collocationOptimized(boolean b)
    {
        return (AccountsManagerPrx)_ice_collocationOptimized(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses twoway invocations.
     * @return A proxy that uses twoway invocations.
     **/
    @Override
    default AccountsManagerPrx ice_twoway()
    {
        return (AccountsManagerPrx)_ice_twoway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses oneway invocations.
     * @return A proxy that uses oneway invocations.
     **/
    @Override
    default AccountsManagerPrx ice_oneway()
    {
        return (AccountsManagerPrx)_ice_oneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch oneway invocations.
     * @return A proxy that uses batch oneway invocations.
     **/
    @Override
    default AccountsManagerPrx ice_batchOneway()
    {
        return (AccountsManagerPrx)_ice_batchOneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses datagram invocations.
     * @return A proxy that uses datagram invocations.
     **/
    @Override
    default AccountsManagerPrx ice_datagram()
    {
        return (AccountsManagerPrx)_ice_datagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch datagram invocations.
     * @return A proxy that uses batch datagram invocations.
     **/
    @Override
    default AccountsManagerPrx ice_batchDatagram()
    {
        return (AccountsManagerPrx)_ice_batchDatagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, except for compression.
     * @param co <code>true</code> enables compression for the new proxy; <code>false</code> disables compression.
     * @return A proxy with the specified compression setting.
     **/
    @Override
    default AccountsManagerPrx ice_compress(boolean co)
    {
        return (AccountsManagerPrx)_ice_compress(co);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection timeout setting.
     * @param t The connection timeout for the proxy in milliseconds.
     * @return A proxy with the specified timeout.
     **/
    @Override
    default AccountsManagerPrx ice_timeout(int t)
    {
        return (AccountsManagerPrx)_ice_timeout(t);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection ID.
     * @param connectionId The connection ID for the new proxy. An empty string removes the connection ID.
     * @return A proxy with the specified connection ID.
     **/
    @Override
    default AccountsManagerPrx ice_connectionId(String connectionId)
    {
        return (AccountsManagerPrx)_ice_connectionId(connectionId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except it's a fixed proxy bound
     * the given connection.@param connection The fixed proxy connection.
     * @return A fixed proxy bound to the given connection.
     **/
    @Override
    default AccountsManagerPrx ice_fixed(com.zeroc.Ice.Connection connection)
    {
        return (AccountsManagerPrx)_ice_fixed(connection);
    }

    static String ice_staticId()
    {
        return "::Office::AccountsManager";
    }
}