import csv

steps = []

def read_csv(filename):
    data = []
    with open(filename, newline='') as file:
        reader = csv.reader(file)
        for row in reader:
            integer = int(row[0])
            alphabet = row[1]
            data.append((integer, alphabet))
    return data

def log_step(data):
    view = ", ".join(f"{x[0]}/{x[1]}" for x in data)
    steps.append(f"[{view}]")

def merge_sort(arr):
    if len(arr) <= 1:
        return arr
    mid = len(arr) // 2
    left = merge_sort(arr[:mid])
    right = merge_sort(arr[mid:])
    merged = merge(left, right)

    log_step(merged)
    return merged

def merge(left, right):
    result = []
    i = j = 0

    while i < len(left) and j < len(right):
        if left[i][0] <= right[j][0]:
            result.append(left[i])
            i += 1
        else:
            result.append(right[j])
            j += 1
    
    result += left[i:]
    result += right[j:]
    return result

if __name__ == "__main__":
    file = input("Enter filename (Start with 'dataset/): ")
    start = int(input("Start row: "))
    end = int(input("Ends row: "))

    to_read = read_csv(file)
    step = to_read[start : end]
    log_step(step)
    merge_sort(step)

    output_file = f"merge_sort_step_{start}_{end}.txt"
    with open(output_file, 'w') as file:
        file.write("\n".join(steps))
    print(f"Steps saved to {output_file}")