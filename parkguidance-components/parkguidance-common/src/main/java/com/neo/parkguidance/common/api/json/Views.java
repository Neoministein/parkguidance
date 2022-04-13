package com.neo.parkguidance.common.api.json;

public class Views {

    public interface Public {

    }

    public interface PublicRelations extends Public {

    }

    public interface Owner extends Public {

    }

    public interface OwnerRelations extends Owner {

    }

    public interface Internal extends Owner {

    }

    public static Class<?> retrieveRelations(Class<?> clazz, boolean relations) {
        if (relations) {
            if (clazz == Public.class) {
                return PublicRelations.class;
            }
            if (clazz == Owner.class) {
                return OwnerRelations.class;
            }
        }
        return clazz;
    }
}
