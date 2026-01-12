
# react-native-fast-contacts

🚀 **Modern, high-performance contact fetcher for React Native.** Built from the ground up using **Turbo Modules (New Architecture)** for maximum speed and efficiency.

A high-performance implementation using JSI (JavaScript Interface) that enables fetching the entire contact list with zero serialization overhead, bypassing the legacy bridge for maximum speed.


## Compatibility & Requirements

This library is optimized for the **React Native New Architecture** and is forward-compatible with the latest versions.

### Minimum Requirements

| Dependency | Version |
| --- | --- |
| **React** | >= 18.0.0 (Supports React 19) |
| **React Native** | >= 0.71.0 (Optimized for 0.83+) |
| **iOS** | 13.0+ |
| **Android** | API 24+ |

> **Note:** This library requires the **New Architecture** to be enabled for best performance.

 
## How It Works

`react-native-fast-contacts` accesses the device contact database directly from native code:

* **iOS:** Uses the `Contacts` framework (Objective-C++).
* **Android:** Uses `ContentResolver` (Kotlin).

### Key Advantages:

* ⚡ **Zero JS Overhead:** Contacts are parsed natively and passed as a ready-to-use array.
* 🔋 **Battery Efficient:** Optimized native queries reduce CPU usage.
* 🧹 **Sanitized Data:** Automatically cleans phone numbers (removes spaces, dashes, etc.).
* 🖼️ **Thumbnail Support:** High-speed retrieval of contact avatars in Base64.



## Installation

```sh
npm install react-native-fast-contacts
# or
yarn add react-native-fast-contacts

```

### iOS Setup

1. Add the following key to your `Info.plist`:
```xml
<key>NSContactsUsageDescription</key>
<string>This app requires access to your contacts to let you connect with friends.</string>

```


2. Install pods:
```sh
cd ios && pod install

```



### Android Setup

The `READ_CONTACTS` permission is handled automatically. However, you **must** request it at runtime as per Android guidelines.



## Usage

```tsx
import React, { useEffect, useState } from 'react';
import { PermissionsAndroid, Platform, FlatList, Text } from 'react-native';
import { getContacts, type Contact } from 'react-native-fast-contacts';

export default function App() {
  const [contacts, setContacts] = useState<Contact[]>([]);

  const requestPermission = async () => {
    if (Platform.OS === 'android') {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.READ_CONTACTS
      );
      return granted === PermissionsAndroid.RESULTS.GRANTED;
    }
    return true;
  };

  useEffect(() => {
    const loadContacts = async () => {
      const hasPermission = await requestPermission();
      if (hasPermission) {
        try {
          const list = await getContacts();
          setContacts(list);
        } catch (error) {
          console.error("Failed to fetch contacts:", error);
        }
      }
    };

    loadContacts();
  }, []);

  return (
    <FlatList
      data={contacts}
      keyExtractor={(item, index) => index.toString()}
      renderItem={({ item }) => (
        <Text>{item.name}: {item.number}</Text>
      )}
    />
  );
}

```

 

## Data Structure

| Field | Type | Description |
| --- | --- | --- |
| `name` | `string` | Full name (Given + Family name) |
| `number` | `string` | Sanitized phone number (digits only) |
| `email` | `string` | Primary email address (if available) |
| `thumbnail` | `string` | Base64 encoded PNG image data URI |

---

## License

MIT
