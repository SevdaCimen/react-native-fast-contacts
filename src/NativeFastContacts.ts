import { TurboModuleRegistry, type TurboModule } from 'react-native';

export interface Contact {
  name: string;
  number: string;
  email?: string;
  thumbnail?: string;
}

export interface Spec extends TurboModule {
  getContacts(): Promise<Object[]>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('FastContacts');
