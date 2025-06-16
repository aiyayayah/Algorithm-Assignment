import os
import time
from typing import List, Optional, NamedTuple

class Pair(NamedTuple):
    number: int
    text: str

def get_user_input() -> str:
    """Get the filename from user input."""
    return input("Enter the file name to perform binary search: ")

def search_file_name(target_filename: str) -> bool:
    """Check if the target filename exists in the output/merge_sort folder."""
    output_folder = "output/merge_sort/"
    
    if not os.path.exists(output_folder) or not os.path.isdir(output_folder):
        print("Output folder not found!")
        print("Please run dataSetGenerator.py to generate dataSets first.")
        return False
    
    file_list = os.listdir(output_folder)
    if not file_list:
        return False
    
    return target_filename in file_list

def parse_file(filename: str) -> Optional[List[Pair]]:
    """Parse the file and return a list of Pair objects."""
    data_list = []
    try:
        with open(filename, 'r') as file:
            for line in file:
                line = line.strip()
                if line:  # Skip empty lines
                    parts = line.split(',', 1)
                    if len(parts) == 2:
                        number = int(parts[0].strip())
                        text = parts[1].strip()
                        data_list.append(Pair(number, text))
        return data_list
    except (IOError, ValueError) as e:
        print(f"Error reading file: {filename}")
        return None

def binary_search_analysis(data_list: List[Pair]) -> int:
    """Perform comprehensive binary search analysis."""
    n = len(data_list)
    output = []
    
    print(f"Total elements: {n}")
    print(n - 1)
    
    # BEST CASE - Middle element
    output.append("BEST CASE: \n")
    start_time = time.perf_counter_ns()
    left = 0
    right = n - 1
    comparisons = 0
    
    while left <= right:
        middle = (right + left) // 2
        comparisons += 1
        break
    
    end_time = time.perf_counter_ns()
    best_time = end_time - start_time
    output.append(f"Comparisons: {comparisons}\n")
    output.append(f"Time: {best_time} nanoseconds \n \n")
    
    # WORST CASE
    output.append("WORST CASE: \n")
    
    # target at LEFTMOST of the list
    start_time = time.perf_counter_ns()
    left = 0
    right = len(data_list) - 1
    comparisons = 0
    while left <= right:
        middle = (left + right) // 2
        comparisons += 1
        if middle == 0:
            break
        right = middle - 1
    end_time = time.perf_counter_ns()
    left_time = end_time - start_time
    left_comparisons = comparisons
    
    output.append("Leftmost Target: \n")
    output.append(f"  Loop: {left_comparisons}\n")
    output.append(f"  Time: {left_time} nanoseconds \n")
    
    # target at RIGHTMOST of the list
    start_time = time.perf_counter_ns()
    left = 0
    right = len(data_list) - 1
    comparisons = 0
    while left <= right:
        middle = (left + right) // 2
        comparisons += 1
        if middle == n - 1:
            break
        left = middle + 1
    end_time = time.perf_counter_ns()
    right_time = end_time - start_time
    right_comparisons = comparisons
    
    output.append("Rightmost Target: \n")
    output.append(f"  Loop: {right_comparisons}\n")
    output.append(f"  Time: {right_time} nanoseconds \n")
    
    # target at left beside the first middle
    start_time = time.perf_counter_ns()
    left = 0
    right = len(data_list) - 1
    first_middle = (left + right) // 2
    target_left_index = first_middle - 1  # One index left of the initial middle
    comparisons = 0
    while left <= right:
        middle = (left + right) // 2
        comparisons += 1
        if middle == target_left_index:
            break
        left = middle + 1
    end_time = time.perf_counter_ns()
    left_beside_middle_time = end_time - start_time
    left_beside_middle_comparisons = comparisons
    
    output.append("Left Beside Middle: \n")
    output.append(f"  Loop: {left_beside_middle_comparisons}\n")
    output.append(f"  Time: {left_beside_middle_time} nanoseconds \n")
    
    # target at right beside the first middle
    start_time = time.perf_counter_ns()
    left = 0
    right = len(data_list) - 1
    target_right_index = first_middle + 1  # One index right of the initial middle
    comparisons = 0
    while left <= right:
        middle = (left + right) // 2
        comparisons += 1
        if middle == target_right_index:
            break
        right = middle - 1
    end_time = time.perf_counter_ns()
    right_beside_middle_time = end_time - start_time
    right_beside_middle_comparisons = comparisons
    
    output.append("Right Beside Middle: \n")
    output.append(f"  Loop: {right_beside_middle_comparisons}\n")
    output.append(f"  Time: {right_beside_middle_time} nanoseconds \n \n")
    
    # AVERAGE CASE
    output.append("AVERAGE CASE: \n")
    total_time = 0
    total_comparisons = 0
    
    for i in range(n):
        target = data_list[i].number
        left = 0
        right = n - 1
        comparisons = 0
        
        start_time = time.perf_counter_ns()
        
        while left <= right:
            middle = (left + right) // 2
            comparisons += 1
            
            if data_list[middle].number == target:
                break
            elif data_list[middle].number < target:
                left = middle + 1
            else:
                right = middle - 1
        
        end_time = time.perf_counter_ns()
        duration = end_time - start_time
        total_time += duration
        total_comparisons += comparisons
        
        # Print and write individual search stats
        output.append(f"Index {i} (target {target}): ")
        output.append(f"Comparisons = {comparisons}, ")
        output.append(f"Time = {duration} ns\n")
    
    average_time = total_time / n
    average_comparisons = total_comparisons / n
    
    output.append(f"Average comparisons: {average_comparisons}\n")
    output.append(f"Average time: {average_time} nanoseconds\n")
    
    write_to_file(f"output/binary_search/binary_search_{n}.txt", "".join(output))
    
    return total_comparisons

def write_to_file(path: str, content: str) -> None:
    """Write content to a file, creating directories if necessary."""
    try:
        # Create parent directories if they don't exist
        os.makedirs(os.path.dirname(path), exist_ok=True)
        
        with open(path, 'w') as file:
            file.write(content)
        
        print(f"Output written to: {path}")
    except IOError as e:
        print("Failed to write output file.")
        print(e)

def main():
    """Main function to execute the binary search analysis program."""
    filename = get_user_input()
    file_found = search_file_name(filename)
    
    if not file_found:
        print(f"{filename} was NOT FOUND in the output folder.")
        return
    
    print(f"{filename} was FOUND in the output folder.")
    
    # Parse the file
    data_list = parse_file(f"output/merge_sort/{filename}")
    if data_list is None:
        print("Failed to read or parse the file.")
        return
    
    binary_search_analysis(data_list)

if __name__ == "__main__":
    main()