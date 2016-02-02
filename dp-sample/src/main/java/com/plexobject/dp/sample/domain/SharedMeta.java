package com.plexobject.dp.sample.domain;

import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;

public class SharedMeta {
    public static MetaField accountId = MetaFieldFactory.create("accountId",
            Account.class.getSimpleName(), MetaFieldType.SCALAR_INTEGER, true);
    public static MetaField symbol = MetaFieldFactory.create("symbol",
            Security.class.getSimpleName(), MetaFieldType.SCALAR_TEXT, true);
    public static MetaField underlyingSymbol = MetaFieldFactory.create(
            "underlyingSymbol", Security.class.getSimpleName(),
            MetaFieldType.SCALAR_TEXT, true);
    public static MetaField exchange = MetaFieldFactory.create("exchange",
            Security.class.getSimpleName(), MetaFieldType.SCALAR_TEXT, true);
    public static MetaField marketSession = MetaFieldFactory.create(
            "marketSession", Security.class.getSimpleName(),
            MetaFieldType.SCALAR_TEXT, false);
    public static MetaField userId = MetaFieldFactory.createInteger("userId",
            User.class.getSimpleName(), true);

}
