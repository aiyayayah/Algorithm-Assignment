import tkinter as tk
from tkinter import filedialog
import csv
import os

def quick_sort(arr, low, high, steps, full_subarray, full_start):
    if low < high:
        pi = partition(arr, low, high, steps, full_subarray, full_start)
        quick_sort(arr, low, pi - 1, steps, full_subarray, full_start)
        quick_sort(arr, pi + 1, high, steps, full_subarray, full_start)

def partition(arr, low, high, steps, full_subarray, full_start):
    pivot = arr[high][0]
    i = low - 1
    for j in range(low, high):
        if arr[j][0] <= pivot:
            i += 1
            arr[i], arr[j] = arr[j], arr[i]
    arr[i + 1], arr[high] = arr[high], arr[i + 1]
    
    for k in range(low, high + 1):
        full_subarray[k - full_start] = arr[k]
    
    steps.append(format_array_state(full_subarray, low - full_start, high - full_start, (i + 1) - full_start))
    return i + 1

def format_array_state(subarray, low, high, pivot_index):
    parts = []
    for i in range(len(subarray)):
        val, s = subarray[i]
        if low <= i <= high:
            if i == pivot_index:
                parts.append(f"->{val}/{s}")
            else:
                parts.append(f"{val}/{s}")
        else:
            parts.append(f"{val}/{s}")
    return f"pi={pivot_index}[{', '.join(parts)}]"


def verify_unchanged_outside_range(original, sorted_arr, low, high):
    for i in range(low):
        if original[i] != sorted_arr[i]:
            return False
    for i in range(high + 1, len(original)):
        if original[i] != sorted_arr[i]:
            return False
    return True

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

    while True:
        try:
            start_row = int(input(f"Enter start row (1 to {n}): "))
            if 1 <= start_row <= n:
                break
            else:
                print("Invalid start row. Please try again.")
        except ValueError:
            print("Invalid input. Please enter a number.")

    while True:
        try:
            end_row = int(input(f"Enter end row ({start_row} to {n}): "))
            if start_row <= end_row <= n:
                break
            else:
                print("Invalid end row. Please try again.")
        except ValueError:
            print("Invalid input. Please enter a number.")

    print(f"Starting quicksort from row {start_row} to {end_row}...")

    low = start_row - 1
    high = end_row - 1

    original_elements = elements.copy()

    steps = []
    full_subarray = elements[low:high + 1]
    quick_sort(elements, low, high, steps, full_subarray, low)


    if not verify_unchanged_outside_range(original_elements, elements, low, high):
        print("Warning: Elements outside the sorting range have been modified.")

    output_dir = "python/output/quick_sort_step"
    os.makedirs(output_dir, exist_ok=True)
    step_filename = os.path.join(output_dir, f"quick_sort_step_{start_row}_{end_row}.txt")
    with open(step_filename, "w") as f:
        f.write("\n".join(steps))

    print(f"Sorting steps written to {step_filename}")

if __name__ == "__main__":
    main()
