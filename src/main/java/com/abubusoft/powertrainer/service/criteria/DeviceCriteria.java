package com.abubusoft.powertrainer.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.Device} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.DeviceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DeviceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter owner;

    private StringFilter deviceUuid;

    public DeviceCriteria() {}

    public DeviceCriteria(DeviceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.owner = other.owner == null ? null : other.owner.copy();
        this.deviceUuid = other.deviceUuid == null ? null : other.deviceUuid.copy();
    }

    @Override
    public DeviceCriteria copy() {
        return new DeviceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOwner() {
        return owner;
    }

    public StringFilter owner() {
        if (owner == null) {
            owner = new StringFilter();
        }
        return owner;
    }

    public void setOwner(StringFilter owner) {
        this.owner = owner;
    }

    public StringFilter getDeviceUuid() {
        return deviceUuid;
    }

    public StringFilter deviceUuid() {
        if (deviceUuid == null) {
            deviceUuid = new StringFilter();
        }
        return deviceUuid;
    }

    public void setDeviceUuid(StringFilter deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeviceCriteria that = (DeviceCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(owner, that.owner) && Objects.equals(deviceUuid, that.deviceUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, deviceUuid);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (owner != null ? "owner=" + owner + ", " : "") +
            (deviceUuid != null ? "deviceUuid=" + deviceUuid + ", " : "") +
            "}";
    }
}
