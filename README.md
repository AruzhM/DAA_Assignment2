# DAA Assignment 2 — Data Structures

**Student:** Marat Aruzhan  
**Group:** SE-2525

Java implementations of a dynamic array, a singly linked list,
and a binary min-heap, with correctness tests, operation counters,
performance experiments, and plots.

## Implemented structures

- **DynamicArray:** append, indexed insertion, indexed removal,
  indexed access, and value search; capacity doubles when full.
- **MyLinkedList:** the same list operations, using head and tail
  references.
- **MinHeap:** insert, peekMin, extractMin, and bottom-up buildHeap.
- **IntList:** a shared interface for the two list implementations.

The structures store integers and do not use standard Java
collections internally.

## Requirements

- JDK 25 — the current project targets Java 25.
- Maven, or the Maven installation bundled with IntelliJ IDEA.
- Python 3 and Matplotlib for regenerating plots.

The experiments were run with OpenJDK 25.0.1 on Windows 11,
amd64. Python 3.13.12 was used for plotting.

## Open in IntelliJ IDEA

1. Open the project folder containing pom.xml.
2. Load the project as a Maven project.
3. Select JDK 25 as the Project SDK and Maven runner JDK.
4. Reload Maven dependencies.

Run all commands and benchmark classes with the project root
as the working directory.

## Run tests

With Maven available in the terminal:

```shell
mvn clean test
```

Alternatively, in IntelliJ IDEA, open the Maven tool window and
run Lifecycle → test.

The test suite includes boundary cases, resizing, duplicate and
negative values, randomized comparisons with standard collections,
heap validity checks, and operation-counter checks.

The last recorded complete test run passed 40 tests with no
failures or errors.

## Run the workload benchmarks

Run the main method of:

```text
src/main/java/org/example/BenchmarkRunner.java
```

In IntelliJ IDEA, click the green Run icon beside its main method.

After compiling, it can also be launched from the project root:

```shell
java -cp target/classes org.example.BenchmarkRunner
```

The workloads are:

| Workload | Description |
|---|---|
| W1 | 10,000 random indexed reads |
| W2 | 1,000 searches, half successful and half unsuccessful |
| W3/head | 1,000 insertion/removal pairs at the head |
| W3/middle | 1,000 insertion/removal pairs at index n / 2 |
| W4 | n heap insertions followed by n minimum extractions |

Input sizes are 100, 1,000, 10,000, and 100,000.
Random inputs use seed 42. Each case uses five warm-up runs
and five measured runs; the median time is reported.

Output:

```text
results/results.csv
```

CSV columns:

```text
workload,variant,structure,n,time_ms,steps,moves,comparisons
```

The file contains 36 data rows. Running the benchmark again
replaces the existing results file.

## Bonus 1: Bottom-up heap construction

Run the main method of HeapBuildBenchmark in IntelliJ IDEA,
or run this command after compilation:

```shell
java -cp target/classes org.example.HeapBuildBenchmark
```

This compares repeated insertion with Floyd's bottom-up buildHeap
on random and descending inputs. It records construction time
and operation counts, with correctness checks outside the timer.

Output:

```text
results/heap_build.csv
```

## Bonus 2: Memory measurement with JOL

The Maven dependency org.openjdk.jol:jol-core:0.17 is declared
in pom.xml.

In IntelliJ IDEA:

1. Open the run configuration for MemoryBenchmark.
2. Enable the VM options field if it is hidden.
3. Add these VM options:

```text
-Djdk.attach.allowAttachSelf=true -XX:+EnableDynamicAgentLoading
```

4. Run MemoryBenchmark.main.

JOL measures the structure and its reachable objects.
The results depend on the JVM's object layout and alignment.

Output files:

```text
results/memory.csv
results/jol_vm.txt
results/jol_footprints.txt
```

JOL may print an Unsafe deprecation warning on JDK 25.
The recorded successful run completed with exit code 0.

## Generate plots

From the project root, create a Python virtual environment
and install Matplotlib:

```shell
python -m venv .venv
```

On Windows PowerShell:

```powershell
.\.venv\Scripts\python.exe -m pip install matplotlib
.\.venv\Scripts\python.exe plot_results.py
```

On Linux or macOS:

```shell
.venv/bin/python -m pip install matplotlib
.venv/bin/python plot_results.py
```

The script reads these existing CSV files:

- results/results.csv
- results/heap_build.csv
- results/memory.csv

It generates 14 PNG figures in results/plots.
Existing figures with the same names are replaced.

Timing plots use logarithmic axes. Positive operation counters
use logarithmic scales, while all-zero counters are shown
explicitly at zero.

## Results and analysis

See [REPORT.md](REPORT.md) for:

- complexity analysis;
- two loop-invariant proofs;
- workload plots and interpretation;
- benchmarking limitations;
- Floyd heap-construction analysis;
- JOL memory analysis.

The operation counters measure selected instrumented operations.
Their precise interpretation and limitations are documented
in the report.

## Main project files

| Path | Purpose |
|---|---|
| src/main/java/org/example/ | Structures, interface, and benchmark runners |
| src/test/java/org/example/ | JUnit tests |
| pom.xml | Maven configuration and dependencies |
| plot_results.py | Plot generation |
| results/ | CSV measurements and JOL details |
| results/plots/ | Generated figures |
| REPORT.md | Analysis and experimental discussion |

Generated build files, IDE settings, and the Python virtual
environment should not be included in the submitted source archive.