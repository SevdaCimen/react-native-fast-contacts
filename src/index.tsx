import FastContacts from './NativeFastContacts';
import type { Contact } from './NativeFastContacts';

export async function getContacts(): Promise<Contact[]> {
  if (!FastContacts) {
    throw new Error('FastContacts module is not available.');
  }

  const rawContacts = await FastContacts.getContacts();

  return (rawContacts as Contact[]).map((contact) => ({
    ...contact,
    number: contact.number ? contact.number.replace(/[\s()\-+]/g, '') : '',
  }));
}

export type { Contact };
