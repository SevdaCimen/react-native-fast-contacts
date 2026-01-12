import { useEffect, useState } from 'react';
import {
  StyleSheet,
  Text,
  View,
  ScrollView,
  Platform,
  PermissionsAndroid,
} from 'react-native';
import { getContacts, type Contact } from 'react-native-fast-contacts';

export default function App() {
  const [contacts, setContacts] = useState<Contact[]>([]);
  const requestPermission = async () => {
    if (Platform.OS === 'android') {
      try {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.READ_CONTACTS,
          {
            title: 'Contacts Permission',
            message: 'This app needs access to your contacts to list them.',
            buttonNeutral: 'Ask Me Later',
            buttonNegative: 'Cancel',
            buttonPositive: 'OK',
          }
        );
        return granted === PermissionsAndroid.RESULTS.GRANTED;
      } catch (err) {
        console.error('❌ Error:', err);
        return false;
      }
    }
    return true;
  };
  useEffect(() => {
    const loadContacts = async () => {
      const hasPermission = await requestPermission();
      if (hasPermission) {
        try {
          const data = await getContacts();
          console.log('✅ Contacts:', data.length);
          setContacts(data);
        } catch (err) {
          console.error('❌ Error:', err);
        }
      } else {
        console.log('🚫 Permission denied');
      }
    };

    loadContacts();
  }, []);

  useEffect(() => {
    getContacts()
      .then((data) => {
        console.log('✅ Contacts Fetched:', data.length);
        setContacts(data);
      })
      .catch((err) => {
        console.error('❌ Error fetching contacts:', err);
      });
  }, []);

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Fast Contacts Test</Text>
        <Text style={styles.subtitle}>{contacts.length} Contacts Found</Text>
      </View>

      <ScrollView contentContainerStyle={styles.scrollContent}>
        {contacts.map((item, index) => (
          <View key={index} style={styles.contactItem}>
            <Text style={styles.contactName}>{item.name || 'No Name'}</Text>
            <Text style={styles.contactNumber}>{item.number}</Text>
          </View>
        ))}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f5f5f5' },
  header: {
    padding: 20,
    alignItems: 'center',
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  title: { fontSize: 20, fontWeight: 'bold' },
  subtitle: { fontSize: 14, color: '#666' },
  scrollContent: { padding: 10 },
  contactItem: {
    backgroundColor: '#fff',
    padding: 15,
    marginBottom: 10,
    borderRadius: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    elevation: 2,
  },
  contactName: { fontSize: 16, fontWeight: '600' },
  contactNumber: { fontSize: 14, color: '#555', marginTop: 4 },
});
