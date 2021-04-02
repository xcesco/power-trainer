import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDevice, getDeviceIdentifier } from '../device.model';

export type EntityResponseType = HttpResponse<IDevice>;
export type EntityArrayResponseType = HttpResponse<IDevice[]>;

@Injectable({ providedIn: 'root' })
export class DeviceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/devices');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(device: IDevice): Observable<EntityResponseType> {
    return this.http.post<IDevice>(this.resourceUrl, device, { observe: 'response' });
  }

  update(device: IDevice): Observable<EntityResponseType> {
    return this.http.put<IDevice>(`${this.resourceUrl}/${getDeviceIdentifier(device) as number}`, device, { observe: 'response' });
  }

  partialUpdate(device: IDevice): Observable<EntityResponseType> {
    return this.http.patch<IDevice>(`${this.resourceUrl}/${getDeviceIdentifier(device) as number}`, device, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDevice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeviceToCollectionIfMissing(deviceCollection: IDevice[], ...devicesToCheck: (IDevice | null | undefined)[]): IDevice[] {
    const devices: IDevice[] = devicesToCheck.filter(isPresent);
    if (devices.length > 0) {
      const deviceCollectionIdentifiers = deviceCollection.map(deviceItem => getDeviceIdentifier(deviceItem)!);
      const devicesToAdd = devices.filter(deviceItem => {
        const deviceIdentifier = getDeviceIdentifier(deviceItem);
        if (deviceIdentifier == null || deviceCollectionIdentifiers.includes(deviceIdentifier)) {
          return false;
        }
        deviceCollectionIdentifiers.push(deviceIdentifier);
        return true;
      });
      return [...devicesToAdd, ...deviceCollection];
    }
    return deviceCollection;
  }
}
