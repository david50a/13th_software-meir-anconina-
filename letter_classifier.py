import pandas as pd
import numpy as np
import os
from PIL import Image
from tqdm import tqdm
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from tensorflow.keras.preprocessing.image import ImageDataGenerator

PATH='letters_dataset'
def extract_img():
    csv_path = r'C:\Users\meira\OneDrive\Pictures\letters\A_Z Handwritten Data\A_Z Handwritten Data.csv'
    data = pd.read_csv(csv_path)
    base_dir = "letters_dataset"
    os.makedirs(base_dir, exist_ok=True)
    counters = [0] * 26
    for i, row in tqdm(data.iterrows(), total=len(data)):
        label = int(row[0])                # 0-25
        letter = chr(label + 65)           # A-Z
        pixels = row[1:].values.reshape(28, 28).astype(np.uint8)
        letter_dir = os.path.join(base_dir, letter)
        os.makedirs(letter_dir, exist_ok=True)
        counters[label] += 1
        filename = f"{letter}_{counters[label]:05d}.png"
        filepath = os.path.join(letter_dir, filename)
        img = Image.fromarray(pixels, mode='L')
        img.save(filepath)
def build_model():
    batch_size=32
    img_size=64,64
    data_gen=ImageDataGenerator(
        rescale=1./255,
        validation_split=0.2,
        shear_range=0.2,
        zoom_range=0.2,
        horizontal_flip=True
    )
    train_data=data_gen.flow_from_directory(
        PATH,
        target_size=img_size,
        batch_size=batch_size,
        class_mode='categorical',
        subset='training'
    )
    val_data = data_gen.flow_from_directory(
        PATH,
        target_size=img_size,
        batch_size=batch_size,
        class_mode='categorical',
        subset='validation'
    )
    model=Sequential([
        Conv2D(32,(3,3),activation='relu',input_shape=(img_size[0],img_size[1],3)),
        MaxPooling2D(pool_size=(2,2)),
        Conv2D(64, (3, 3), activation='relu'),
        MaxPooling2D(pool_size=(2, 2)),

        Conv2D(128, (3, 3), activation='relu'),
        MaxPooling2D(pool_size=(2, 2)),

        Flatten(),
        Dense(128, activation='relu'),
        Dropout(0.5),
        Dense(train_data.num_classes, activation='softmax')
    ])
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
    model.save("fruit_classifier_model.h5")
    print("Model saved!")
build_model()
