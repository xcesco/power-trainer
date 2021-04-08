export interface IDevice {
  id?: number;
  owner?: string;
  deviceUuid?: string;
}

export class Device implements IDevice {
  constructor(public id?: number, public owner?: string, public deviceUuid?: string) {}
}

export function getDeviceIdentifier(device: IDevice): number | undefined {
  return device.id;
}
