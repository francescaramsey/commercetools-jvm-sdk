package io.sphere.sdk.zones.queries;

import io.sphere.sdk.customers.queries.LocationsCollectionQueryModel;
import io.sphere.sdk.queries.DefaultModelQueryModelImpl;
import io.sphere.sdk.queries.QueryModel;
import io.sphere.sdk.queries.StringQuerySortingModel;
import io.sphere.sdk.zones.Zone;

import java.util.Optional;

public class ZoneQueryModel extends DefaultModelQueryModelImpl<Zone> {
    public static ZoneQueryModel of() {
        return new ZoneQueryModel(Optional.<QueryModel<Zone>>empty(), Optional.<String>empty());
    }

    private ZoneQueryModel(final Optional<? extends QueryModel<Zone>> parent, final Optional<String> pathSegment) {
        super(parent, pathSegment);
    }

    public StringQuerySortingModel<Zone> name() {
        return new StringQuerySortingModel<>(Optional.of(this), "name");
    }

    public LocationsCollectionQueryModel<Zone> locations() {
        return new LocationsCollectionQueryModel<>(Optional.of(this), "locations");
    }
}
