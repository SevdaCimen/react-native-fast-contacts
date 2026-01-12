#import "FastContacts.h"
#import <Contacts/Contacts.h>

@implementation FastContacts

RCT_EXPORT_MODULE()

- (void)getContacts:(RCTPromiseResolveBlock)resolve
             reject:(RCTPromiseRejectBlock)reject
{
  CNContactStore *store = [CNContactStore new];

  [store requestAccessForEntityType:CNEntityTypeContacts
                  completionHandler:^(BOOL granted, NSError *error) {

    if (!granted) {
      resolve(@[]);
      return;
    }

    NSArray *keys = @[
      CNContactGivenNameKey,
      CNContactFamilyNameKey,
      CNContactPhoneNumbersKey,
      CNContactEmailAddressesKey,
      CNContactThumbnailImageDataKey
    ];

    NSMutableArray *result = [NSMutableArray array];
    CNContactFetchRequest *request =
      [[CNContactFetchRequest alloc] initWithKeysToFetch:keys];

    NSError *fetchError = nil;

    BOOL success =
      [store enumerateContactsWithFetchRequest:request
                                         error:&fetchError
                                    usingBlock:^(CNContact *contact, BOOL *stop) {

      NSString *fullName =
        [NSString stringWithFormat:@"%@ %@",
          contact.givenName ?: @"",
          contact.familyName ?: @""];

      NSString *number = @"";
      if (contact.phoneNumbers.count > 0) {
        number = contact.phoneNumbers.firstObject.value.stringValue ?: @"";
      }

      NSString *email = @"";
      if (contact.emailAddresses.count > 0) {
        email = contact.emailAddresses.firstObject.value ?: @"";
      }

      NSString *thumbnail = @"";
      if (contact.thumbnailImageData) {
        NSString *base64 =
          [contact.thumbnailImageData base64EncodedStringWithOptions:0];
        thumbnail =
          [NSString stringWithFormat:@"data:image/png;base64,%@", base64];
      }

      [result addObject:@{
        @"name": fullName,
        @"number": number,
        @"email": email,
        @"thumbnail": thumbnail
      }];
    }];

    if (success) {
      resolve(result);
    } else {
      reject(@"fetch_error",
             @"Failed to fetch contacts",
             fetchError);
    }
  }];
}

// TurboModule için gerekli JSI köprüsü
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
  (const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<
    facebook::react::NativeFastContactsSpecJSI>(params);
}

@end