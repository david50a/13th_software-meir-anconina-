import cv2
import os

def open_img(path):
    img = cv2.imread(path)
    if img is None:
        print("Image not found or failed to load.")
        return

    # Resize the image
    resized_img = cv2.resize(img, (img.shape[1] // 2, img.shape[0] // 2))

    # Convert to grayscale
    gray_image = cv2.cvtColor(resized_img, cv2.COLOR_BGR2GRAY)

    # Display the grayscale image
    cv2.imshow('Grayscale', gray_image)

    # Save the grayscale image
    save_path = os.path.splitext(path)[0] + '_grayscale.png'
    cv2.imwrite(save_path, gray_image)
    print(f"Grayscale image saved to: {save_path}")

    # Wait for key press and close windows
    cv2.waitKey(0)
    cv2.destroyAllWindows()

open_img(r'C:\Users\meira\Downloads\cds.png')
