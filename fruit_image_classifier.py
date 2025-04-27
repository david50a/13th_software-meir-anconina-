import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import matplotlib.pyplot as plt

# 1. Setup paths
#determine the size of the images and how mwny imges will send to the model for training each time
dataset_path = r'C:\Users\meira\OneDrive\Documents\fruits\fruits-360_100x100\fruits-360\Training'
img_size = (64, 64)
batch_size = 32

# 2. Load and preprocess images
# make the image smaller
# split some images for checking
# move the image
# make a zoom in the picture
#make a horizontal flip
datagen = ImageDataGenerator(
    rescale=1./255,
    validation_split=0.2,
    shear_range=0.2,
    zoom_range=0.2,
    horizontal_flip=True
)
#take the images from the path and learn tham by categorical
train_data = datagen.flow_from_directory(
    dataset_path,
    target_size=img_size,
    batch_size=batch_size,
    class_mode='categorical',
    subset='training'
)
#testing if the model learn correctly
val_data = datagen.flow_from_directory(
    dataset_path,
    target_size=img_size,
    batch_size=batch_size,
    class_mode='categorical',
    subset='validation'
)

# 3. Build the CNN model
#stack the layers that have 32 like scanners the each scanner look at 3*3 pixels of 3 colors
# make the images smaller for picking the strongest part of the image
# scan with 64 more advances scanner the images
# again shrink the images for keeping the strongest parts
#scan with 128 more powerful scanners
#shring again
#built a list of numbers for make a facilitate the understanding for the model
# make a nural  network with 128 cells
# turn off 50% of the cells for a better training
#built a decision layer and the the most probably option choosen to the output
model = Sequential([
    Conv2D(32, (3, 3), activation='relu', input_shape=(img_size[0], img_size[1], 3)),
    MaxPooling2D(pool_size=(2, 2)),

    Conv2D(64, (3, 3), activation='relu'),
    MaxPooling2D(pool_size=(2, 2)),

    Conv2D(128, (3, 3), activation='relu'),
    MaxPooling2D(pool_size=(2, 2)),

    Flatten(),
    Dense(128, activation='relu'),
    Dropout(0.5),
    Dense(train_data.num_classes, activation='softmax')  # Output layer matches number of classes
])
# use the adam way to teach the model, by give him "bad" fealing if the model is wrong and mesure the time that takes the model for a right guess
# 4. Compile the model
model.compile(
    optimizer='adam',
    loss='categorical_crossentropy',
    metrics=['accuracy']
)

# 5. Train the model
history = model.fit(
    train_data,
    validation_data=val_data,
    epochs=10
)

# 6. Save the model
model.save("fruit_classifier_model.h5")
print("Model saved!")

# 7. Plot accuracy/loss graphs
plt.figure(figsize=(12, 5))
plt.subplot(1, 2, 1)
plt.plot(history.history['accuracy'], label='Train Acc')
plt.plot(history.history['val_accuracy'], label='Val Acc')
plt.title('Accuracy')
plt.legend()

plt.subplot(1, 2, 2)
plt.plot(history.history['loss'], label='Train Loss')
plt.plot(history.history['val_loss'], label='Val Loss')
plt.title('Loss')
plt.legend()

plt.tight_layout()
plt.show()
# meir anconina
