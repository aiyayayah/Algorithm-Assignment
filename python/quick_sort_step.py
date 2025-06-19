import tkinter as tk
from tkinter import filedialog
import csv
import os

def quick_sort(arr, low, high, steps):
    if low < high:
        pi = partition(arr, low, high, steps)
        quick_sort(arr, low, pi - 1, steps)
        quick_sort(arr, pi + 1, high, steps)

def partition(arr, low, high, steps):
    pivot = arr[high][0]
    i = low - 1
    for j in range(low, high):
        if arr[j][0] <= pivot:
            i += 1
            arr[i], arr[j] = arr[j], arr[i]
    arr[i + 1], arr[high] = arr[high], arr[i + 1]
    steps.append(get_array_state(arr, low, high, i + 1))
    return i + 1

def get_array_state(arr, low, high, pivot_index):
    state_lines = []
    state_lines.append(f"Current array state between indexes {low} and {high}:")
    for i in range(low, high + 1):
        val, s = arr[i]
        if i == pivot_index:
            state_lines.append(f"-> ({val}, {s}) [pivot]")
        else:
            state_lines.append(f"   ({val}, {s})")
    state_lines.append("")
    return "\n".join(state_lines)

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
            start_row = int(input(f"Enter start row (0 to {n - 1}): "))
            if 0 <= start_row < n:
                break
            else:
                print("Invalid start row. Please try again.")
        except ValueError:
            print("Invalid input. Please enter a number.")

    while True:
        try:
            end_row = int(input(f"Enter end row ({start_row} to {n - 1}): "))
            if start_row <= end_row < n:
                break
            else:
                print("Invalid end row. Please try again.")
        except ValueError:
            print("Invalid input. Please enter a number.")

    print(f"Starting quicksort from row {start_row} to {end_row}...")

    steps = []
    quick_sort(elements, start_row, end_row, steps)

    output_dir = "output/quick_sort_step"
    os.makedirs(output_dir, exist_ok=True)
    step_filename = os.path.join(output_dir, f"quick_sort_step_{start_row}_{end_row}.txt")
    with open(step_filename, "w") as f:
        f.write("\n".join(steps))

    print(f"Sorting steps written to {step_filename}")

if __name__ == "__main__":
    main()