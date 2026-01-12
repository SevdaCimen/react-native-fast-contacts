
```markdown
# react-native-fast-contacts

🚀 **Fastest and Modern Contact Fetcher for React Native.** Built with **TurboModule** (New Architecture) for maximum performance, supporting React Native 0.72+ and 0.83+ (including React 19).

## Features
- 🏎️ **TurboModule Support:** High-performance native bridge.
- 📱 **Modern Architecture:** Written in Kotlin (Android) and Objective-C++ (iOS).
- 🧹 **Clean Data:** Automatically sanitizes phone numbers.
- 🖼️ **Thumbnail Support:** Fetches contact photos in Base64 format.

---

## Installation

```sh
npm install react-native-fast-contacts
# or
yarn add react-native-fast-contacts

```

### iOS Setup

Add the following key to your `Info.plist`:

```xml
<key>NSContactsUsageDescription</key>
<string>This app requires access to your contacts to let you connect with your friends.</string>
```

Then run:

```sh
cd ios && pod install
```

### Android Setup

Add the following permission to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

---

## Usage

On Android, you **must** request the `READ_CONTACTS` permission at runtime before calling `getContacts()`. On iOS, the permission is handled automatically by the native module upon the first call.

### Example

```tsx
import { useEffect, useState } from 'react';
import { PermissionsAndroid, Platform } from 'react-native';
import { getContacts, type Contact } from 'react-native-fast-contacts';

export default function App() {
  const [contacts, setContacts] = useState<Contact[]>([]);

  const requestAndroidPermission = async () => {
    if (Platform.OS === 'android') {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.READ_CONTACTS
      );
      return granted === PermissionsAndroid.RESULTS.GRANTED;
    }
    return true;
  };

  useEffect(() => {
    const fetchContacts = async () => {
      const hasPermission = await requestAndroidPermission();
      if (hasPermission) {
        try {
          const data = await getContacts();
          setContacts(data);
        } catch (error) {
          console.error("Error fetching contacts:", error);
        }
      }
    };

    fetchContacts();
  }, []);

  // ... render your list
}

```

### Data Structure

The `getContacts()` method returns an array of `Contact` objects:
| Property | Type | Description |
| :--- | :--- | :--- |
| `name` | `string` | Full name of the contact |
| `number` | `string` | Cleaned phone number (sanitized) |
| `email` | `string` | Primary email address |
| `thumbnail` | `string` | Base64 encoded contact image |

---

## License

MIT

