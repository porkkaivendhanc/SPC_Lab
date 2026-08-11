import pandas as pd

print("========================================")
print("PART 1: DATA MASKING")
print("========================================")

data = pd.DataFrame({
    "Name": ["John Doe", "Jane Smith", "Michael Johnson"],
    "Email": [
        "johndoe@example.com",
        "janesmith@example.com",
        "michaeljohnson@example.com"
    ],
    "Age": [25, 30, 35]
})

print("\nOriginal Dataset:")
print(data)

data_masked = data.copy()

data_masked["Name"] = "XXXXXXXXXX"
data_masked["Email"] = "xxxxxxxxxxxxxxxxxxxxxxxx"

print("\nMasked Dataset:")
print(data_masked)

print("\n========================================")
print("PART 2: K-ANONYMIZATION")
print("========================================")

k_data = pd.DataFrame({
    "Name": ["John Doe", "Jane Smith", "Michael Johnson"],
    "Zip Code": ["12345", "67890", "54321"],
    "Age": [25, 30, 35]
})

print("\nOriginal Dataset:")
print(k_data)

k_anonymous = k_data.copy()

k_anonymous["Name"] = "Anonymous"
k_anonymous["Zip Code"] = "XXXXX"

print("\nK-Anonymized Dataset:")
print(k_anonymous)

print("\n========================================")
print("RESULT")
print("========================================")
print("Data anonymization completed successfully.")