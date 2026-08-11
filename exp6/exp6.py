from Crypto.Cipher import AES
from Crypto.Util.Padding import pad
import os

# AES encryption key
# AES supports 16, 24, or 32 byte keys
key = b"ThisIsASecretKey1234567890123456"


def encrypt_image(input_file, output_file):
    # Read the original image
    with open(input_file, "rb") as file:
        image_data = file.read()

    # Generate a random Initialization Vector (IV)
    iv = os.urandom(16)

    # Create AES cipher using CBC mode
    cipher = AES.new(key, AES.MODE_CBC, iv)

    # Add padding to the image data
    padded_data = pad(image_data, AES.block_size)

    # Encrypt the image data
    encrypted_data = cipher.encrypt(padded_data)

    # Save IV and encrypted data
    with open(output_file, "wb") as file:
        file.write(iv)
        file.write(encrypted_data)

    print("Image encrypted successfully!")
    print("Output file:", output_file)


# Encrypt the PNG image
encrypt_image("original_image.png", "encrypted_image.bin")