package com.eclubprague.iot.android.weissmydeweiss.cloud.gsonmods;

import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.ext.gson.GsonConverter;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;

import java.util.List;

/**
 * Created by paulos on 21. 7. 2015.
 */
public class GsonCustomConverter extends GsonConverter {

    /** Variant with media type application/json. */
    private static final VariantInfo VARIANT_JSON = new VariantInfo(
            MediaType.APPLICATION_JSON);

    @Override
    protected <T> GsonRepresentation<T> create(T source) {
        return new GsonCustomRepresentation<T>(source);
    }

    @Override
    protected <T> GsonRepresentation<T> create(Representation source, Class<T> objectClass) {
        return new GsonCustomRepresentation<T>(source, objectClass);
    }

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;

        if (VARIANT_JSON.isCompatible(source)) {
            result = addObjectClass(result, Object.class);
            result = addObjectClass(result, GsonCustomRepresentation.class);
        }

        return result;
    }

}
