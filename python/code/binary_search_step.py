import os
from typing import List, Optional, NamedTuple
import tkinter as tk
from tkinter import filedialog

class Pair(NamedTuple):
    number: int
    text: str

def select_file_with_dialog() -> Optional[str]:
    root = tk.Tk()
    root.withdraw()  # Hide the root window
    file_path = filedialog.askopenfilename(
        title="Select dataset file",
        filetypes=[("CSV Files", "*.csv"), ("Text Files", "*.txt"), ("All Files", "*.*")]
    )
    return file_path if file_path else None

def parse_file(filepath: str) -> Optional[List[Pair]]:
    """Parse the file and return a list of Pair objects."""
    data_list = []
    try:
        with open(filepath, 'r') as file:
            for line in file:
                line = line.strip()
                if line:
                    parts = line.split(",", 1)
                    if len(parts) == 2:
                        number = int(parts[0].strip())
                        text = parts[1].strip()
                        data_list.append(Pair(number, text))
        return data_list
    except (IOError, ValueError):
        print(f"Error reading file: {filepath}")
        return None

def binary_search(data_list: List[Pair], target: int, steps: List[str]) -> int:
    left, right = 0, len(data_list) - 1
    while left <= right:
        middle = (left + right) // 2
        current = data_list[middle].number
        steps.append(f"{middle + 1}: {data_list[middle].number}/{data_list[middle].text}")

        if current == target:
            return middle
        elif current > target:
            right = middle - 1
        else:
            left = middle + 1
    steps.append("-1")
    return -1

def write_steps_to_file(steps: List[str], target: int, found_index: int):
    base_dir = os.getcwd()
    python_output_dir = os.path.join(base_dir, "python", "output", "binary_search_step")
    os.makedirs(python_output_dir, exist_ok=True)

    result_suffix = "(target found)" if found_index != -1 else "(target not found)"
    output_filename = f"binary_search_step_{target}.txt {result_suffix}"
    output_path = os.path.join(python_output_dir, output_filename)

    with open(output_path, "w") as f:
        for step in steps:
            f.write(step + "\n")

    print(f"Steps written to: {output_path}")




def main():
    filepath = select_file_with_dialog()
    if not filepath:
        print("No file selected. Exiting.")
        return

    data_list = parse_file(filepath)
    if not data_list:
        print("Failed to read or parse the file.")
        return

    try:
        target_input = input("Enter a number to search: ")
        target = int(target_input)
    except ValueError:
        print("Invalid input. Please enter an integer.")
        return

    steps = []
    found_index = binary_search(data_list, target, steps)
    write_steps_to_file(steps, target, found_index)

if __name__ == "__main__":
    main()
