import csv
import time

def read_csv(filename):
    data = []
    with open(filename, newline='') as file:
          reader = csv.reader(file)
          for row in reader:
               number = int(row[0])
               text = row[1]
               data.append((number, text))
    return data

def write_csv(data, filename):
     with open(filename, 'w', newline='') as file:
          writer = csv.writer(file)
          writer.writerows(data)

def merge_sort(arr):
    if len(arr) <= 1:
        return arr  #return the list when it's size 0 or 1
    
    mid = len(arr) // 2
    left = merge_sort(arr[:mid])
    right = merge_sort(arr[mid:])
    return merge(left, right)

def merge(left, right):
        result = []
        i = j = 0
        while i < len(left) and j < len(right):
            # find integer
            if left[i][0] <= right[j][0]:
                result.append(left[i])
                i += 1
            else:
                result.append(right[j])
                j += 1
                    
        while i < len(left):
             result.append(left[i])
             i += 1
        while j < len(right):
             result.append(right[j])
             j += 1

        result.extend(left[i:])
        result.extend(right[j:])
        return result

if __name__ == "__main__":
    filename = input("Enter dataset filename: ").strip()
    filepath = f"dataset/{filename}"
    data = read_csv(filepath)

    sort_data = merge_sort(data)
    start = time.time()
    merge_sort(sort_data)
    end = time.time()

    
    output_file = f"merge_sort_{len(sort_data)}.csv"
    write_csv(sort_data, output_file)

    print(f"Sorted data written to: {output_file}")
    print(f"Running time: {end - start:.3f} seconds")