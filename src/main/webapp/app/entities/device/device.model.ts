export interface IDevice {
  id?: number;
  owner?: string;
  deviceId?: string;
}

export class Device implements IDevice {
  constructor(public id?: number, public owner?: string, public deviceId?: string) {}
}

export function getDeviceIdentifier(device: IDevice): number | undefined {
  return device.id;
}
