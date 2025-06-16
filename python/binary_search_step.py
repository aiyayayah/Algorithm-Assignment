import os
from typing import List, Optional, NamedTuple

class Pair(NamedTuple):
    number: int
    text: str

def get_user_input() -> str:
    """Get the filename from user input."""
    return input("Enter the file name to perform binary search step: ")

def search_file_name(target_filename: str) -> bool:
    """Check if the target filename exists in the output folder."""
    output_folder = "output"
    
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

def binary_search(data_list: List[Pair], target: int) -> int:
    """Perform binary search on the list and print each step."""
    left = 0
    right = len(data_list) - 1
    
    while left <= right:
        middle = (left + right) // 2
        current = data_list[middle].number
        
        # Print current step
        print(f"{middle}: {data_list[middle].number}/{data_list[middle].text}")
        
        if current == target:
            return middle
        elif current > target:
            right = middle - 1
        else:
            left = middle + 1
    
    print("-1")
    return -1

def main():
    """Main function to execute the binary search program."""
    filename = get_user_input()
    file_found = search_file_name(filename)
    
    if not file_found:
        print(f"{filename} was NOT FOUND in the output folder.")
        return
    
    print(f"{filename} was FOUND in the output folder.")
    
    target_input = input("Enter a number to search: ")
    
    # Parse the file
    data_list = parse_file(f"output/{filename}")
    if data_list is None:
        print("Failed to read or parse the file.")
        return
    
    try:
        target = int(target_input)
        binary_search(data_list, target)
    except ValueError:
        print("Invalid number entered. Please enter a valid number.")

if __name__ == "__main__":
    main()