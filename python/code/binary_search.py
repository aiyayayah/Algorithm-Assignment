import csv
import time
import os
from tkinter import filedialog, messagebox
import tkinter as tk
from typing import List, Tuple


class Pair:
    def __init__(self, number: int, text: str):
        self.number = number
        self.text = text


def parse_file(filename: str) -> List[Pair]:
    """Parse CSV file and return list of Pair objects."""
    data_list = []
    try:
        with open(filename, 'r', newline='', encoding='utf-8') as file:
            reader = csv.reader(file)
            for row in reader:
                if len(row) >= 2:
                    number = int(row[0].strip())
                    text = row[1].strip()
                    data_list.append(Pair(number, text))
        return data_list
    except (IOError, ValueError) as e:
        print(f"Error reading file: {filename}")
        print(f"Error details: {e}")
        return None


def binary_search_single(data_list: List[Pair], target: int) -> int:
    """Perform binary search and return number of comparisons."""
    left = 0
    right = len(data_list) - 1
    comparisons = 0
    
    while left <= right:
        middle = (left + right) // 2
        comparisons += 1
        
        middle_value = data_list[middle].number
        if middle_value == target:
            return comparisons
        elif middle_value < target:
            left = middle + 1
        else:
            right = middle - 1
    
    return comparisons


def binary_search_analysis(data_list: List[Pair]) -> int:
    """Perform comprehensive binary search analysis."""
    output = []
    input_list_size = len(data_list)
    TIMING_ITERATIONS = 1000
    
    # ---------------------------- WARM UP -----------------------------
    print("Warming up...")
    for i in range(1000):
        binary_search_single(data_list, data_list[i % input_list_size].number)
    
    # ---------------------------- BEST CASE -----------------------------
    left = 0
    right = len(data_list) - 1
    best_case_target = data_list[(left + right) // 2].number
    
    best_case_start_time = time.perf_counter()
    best_case_comparisons = 0
    for i in range(TIMING_ITERATIONS):
        best_case_comparisons = binary_search_single(data_list, best_case_target)
    best_case_end_time = time.perf_counter()
    
    best_case_run_time = (best_case_end_time - best_case_start_time) * 1000 / TIMING_ITERATIONS
    output.append(f"Best Case Time   : {best_case_run_time:.6f} ms")
    
    # ---------------------------- WORST CASE -----------------------------
    worst_case_targets = []
    max_comparisons = 0
    
    # Find elements requiring maximum comparisons
    for i in range(input_list_size):
        comparisons = binary_search_single(data_list, data_list[i].number)
        if comparisons > max_comparisons:
            max_comparisons = comparisons
            worst_case_targets.clear()
            worst_case_targets.append(data_list[i].number)
        elif comparisons == max_comparisons:
            worst_case_targets.append(data_list[i].number)
    
    # Value greater than all elements
    target_greater = data_list[input_list_size - 1].number + 1
    comparisons_greater = binary_search_single(data_list, target_greater)
    if comparisons_greater >= max_comparisons:
        if comparisons_greater > max_comparisons:
            max_comparisons = comparisons_greater
            worst_case_targets.clear()
        worst_case_targets.append(target_greater)
    
    # Value smaller than all elements
    target_smaller = data_list[0].number - 1
    comparisons_smaller = binary_search_single(data_list, target_smaller)
    if comparisons_smaller >= max_comparisons:
        if comparisons_smaller > max_comparisons:
            max_comparisons = comparisons_smaller
            worst_case_targets.clear()
        worst_case_targets.append(target_smaller)
    
    worst_case_start_time = time.perf_counter()
    for i in range(TIMING_ITERATIONS):
        binary_search_single(data_list, worst_case_targets[i % len(worst_case_targets)])
    worst_case_end_time = time.perf_counter()
    
    worst_case_run_time = (worst_case_end_time - worst_case_start_time) * 1000 / TIMING_ITERATIONS
    output.append(f"Worst Case Time  : {worst_case_run_time:.6f} ms")
    
    # ---------------------------- AVERAGE CASE -----------------------------
    total_comparisons = 0
    total_search_time = 0
    
    for i in range(input_list_size):
        target = data_list[i].number
        
        start_time = time.perf_counter()
        comparisons = binary_search_single(data_list, target)
        end_time = time.perf_counter()
        
        total_search_time += (end_time - start_time)
        total_comparisons += comparisons
    
    total_time_ms = total_search_time * 1000
    average_time_ms = total_time_ms / input_list_size
    average_comparisons = total_comparisons / input_list_size
    
    output.append(f"Average Case Time: {average_time_ms:.6f} ms")
    
    # Write results to file
    write_to_file(f"python/output/binary_search/binary_search_{input_list_size}.txt", "\n".join(output))
    
    return total_comparisons


def write_to_file(path: str, content: str):
    """Write content to file, creating directories if needed."""
    try:
        os.makedirs(os.path.dirname(path), exist_ok=True)
        with open(path, 'w', encoding='utf-8') as file:
            file.write(content)
        print(f"Output written to: {path}")
    except IOError as e:
        print("Failed to write output file.")
        print(f"Error: {e}")


def main():
    """Main function to handle file selection and run analysis."""
    # Create root window and hide it
    root = tk.Tk()
    root.withdraw()
    
    # Use file dialog to select CSV file
    file_path = filedialog.askopenfilename(
        title="Select a CSV dataset file for binary search",
        filetypes=[("CSV files", "*.csv"), ("All files", "*.*")]
    )
    
    if file_path:
        print(f"File FOUND: {file_path}")
        
        data_list = parse_file(file_path)
        if data_list is None:
            print("Failed to read or parse the file.")
            return
        
        if len(data_list) == 0:
            print("No data found in the file.")
            return
        
        binary_search_analysis(data_list)
    else:
        print("No file selected. Exiting.")
    
    root.destroy()


if __name__ == "__main__":
    main()