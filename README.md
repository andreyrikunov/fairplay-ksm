# fairplay-ksm [![Build Status](https://travis-ci.org/andreyrikunov/fairplay-ksm.svg?branch=master)](https://travis-ci.org/andreyrikunov/fairplay-ksm)

Apple FairPlay KSM (Key Security Module) module Java based implementation as per **FairPlay Streaming Programming Guide**.

Implements full KSM functionality:

1. Receive an SPC message from an app running on an Apple app/device and parse it.
2. Check the SPC's certificate hash value against the AC.
3. Decrypt the SPC payload.
4. Verify that the Apple app/device is using a supported version of FPS software.
5. Decrypt the session key and random value block in the SPC payload.
6. Check the integrity of the SPC message.
7. Encrypt the content key.
8. Assemble the contents of the CKC payload.
9. Encrypt the CKC payload.
10. Construct the CKC message and send it to the app on the Apple device.
11. Renting and leasing [In progress]
12. Offline playback [In progress]
13. Error codes [In progress]

**FAQ**

1. Can the development credentials in the FairPlay Streaming Server SDK be used for streaming protected 
content using FPS secure key delivery?

    https://developer.apple.com/library/content/qa/qa1967/_index.html#//apple_ref/doc/uid/DTS40017673

2. How to debug FairPlay Streaming?

    https://developer.apple.com/library/content/technotes/tn2454/_index.html