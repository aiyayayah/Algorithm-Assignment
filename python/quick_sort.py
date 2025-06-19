import tkinter as tk
from tkinter import filedialog
import csv
import os
import time

def quick_sort(arr, low, high):
    if low < high:
        pi = partition(arr, low, high)
        quick_sort(arr, low, pi - 1)
        quick_sort(arr, pi + 1, high)

def partition(arr, low, high):
    pivot = arr[high][0]
    i = low - 1
    for j in range(low, high):
        if arr[j][0] <= pivot:
            i += 1
            arr[i], arr[j] = arr[j], arr[i]
    arr[i + 1], arr[high] = arr[high], arr[i + 1]
    return i + 1

def main():
    root = tk.Tk()
    root.withdraw()

    file_path = filedialog.askopenfilename(
        title="Select a CSV dataset file to sort",
        filetypes=[("CSV files", "*.csv")]
    )
    if not file_path:
        print("No file selected. Exiting.")
        return

    elements = []
    with open(file_path, newline='') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            if len(row) >= 2:
                try:
                    value = int(row[0].strip())
                    string_val = row[1].strip()
                    elements.append((value, string_val))
                except ValueError:
                    print(f"Skipping invalid integer in line: {row}")
            else:
                print(f"Skipping invalid line (missing string): {row}")

    if not elements:
        print("No valid integer-string pairs found in the dataset.")
        return

    n = len(elements)

    start_time = time.time()
    quick_sort(elements, 0, n - 1)
    end_time = time.time()

    output_dir = "output/quick_sort"
    os.makedirs(output_dir, exist_ok=True)
    output_filename = os.path.join(output_dir, f"quick_sort_{n}.csv")
    with open(output_filename, "w", newline='') as csvfile:
        writer = csv.writer(csvfile)
        for val, s in elements:
            writer.writerow([val, s])

    print(f"Sorted dataset written to {output_filename}")
    print(f"Quick sort running time: {end_time - start_time:.3f} seconds")

if __name__ == "__main__":
    main()