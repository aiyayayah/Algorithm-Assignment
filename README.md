## 🧠 Overview

This project is developed for the course **CCP6214 Algorithm Design and Analysis**.  
It aims to analyze and compare the performance of two sorting algorithms—**Merge Sort** and **Quick Sort** (last element as pivot)—and evaluate **Binary Search** under **best**, **average**, and **worst** case scenarios.

All implementations are done in **both Java and Python**, strictly following the requirement of **not using built-in sort or search functions**.

---

## 🔧 Implemented Algorithms

### 📦 Dataset Generator
- **File**: `dataset_generator.java`
- **Description**: Generates a CSV file containing random, unique `(integer, string)` pairs
- **Supports**: Dataset sizes up to **1 billion elements**
- **Output format**:

---

### 📚 Sorting Algorithms

| Algorithm           | File               | Input                                  | Output Description                         |
|---------------------|--------------------|----------------------------------------|--------------------------------------------|
| Merge Sort (Steps)  | `merge_sort_step`  | Dataset, Start Row, End Row            | Step-by-step sorting log                   |
| Quick Sort (Steps)  | `quick_sort_step`  | Dataset, Start Row, End Row            | Step-by-step sorting log                   |
| Merge Sort (Full)   | `merge_sort`       | Entire Dataset                         | Sorted dataset CSV (by integer key)        |
| Quick Sort (Full)   | `quick_sort`       | Entire Dataset                         | Sorted dataset CSV (by integer key)        |

---

### 🔍 Searching Algorithms

| Algorithm              | File                 | Input                          | Output Description                              |
|------------------------|----------------------|--------------------------------|-------------------------------------------------|
| Binary Search (Steps)  | `binary_search_step` | Sorted dataset (from QuickSort) | Search trace showing comparison path           |
| Binary Search (Timing) | `binary_search`      | Sorted dataset (from MergeSort) | Best, average, and worst-case timing results    |


## 👥 Team Members & Contributions

- [@aiyayayah](https://github.com/aiyayayah)  
  → `dataset_generator` (Java), `binary_search`, and `binary_search_step` (Java & Python)

- [@yuhang722](https://github.com/yuhang722)  
  → `quick_sort` and `quick_sort_step` (Java & Python)

- [@yithung18](https://github.com/yithung18)  
  → `merge_sort` and `merge_sort_step` (Java & Python)
