import csv
from pathlib import Path

import matplotlib

matplotlib.use("Agg")

import matplotlib.pyplot as plt
from matplotlib.ticker import LogLocator, LogFormatterMathtext


ROOT = Path(__file__).resolve().parent
RESULTS = ROOT / "results"
PLOTS = RESULTS / "plots"

SIZES = [100, 1000, 10000, 100000]

COLORS = {
    "DynamicArray": "tab:blue",
    "MyLinkedList": "tab:orange",
    "MinHeap": "tab:green",
}

STYLES = {
    "-": ("o", "-"),
    "head": ("o", "-"),
    "middle": ("s", "--"),
}

plt.rcParams.update({
    "font.size": 10,
    "figure.dpi": 120,
    "axes.facecolor": "white",
    "figure.facecolor": "white",
})


def read_csv(filename):
    path = RESULTS / filename

    with path.open(encoding="utf-8-sig", newline="") as file:
        return list(csv.DictReader(file))


def configure_axis(ax, ylabel, logarithmic_y=False):
    ax.set_xscale("log")
    ax.set_xticks(SIZES)
    ax.xaxis.set_major_formatter(LogFormatterMathtext())

    ax.set_xlabel("n (input size)")
    ax.set_ylabel(ylabel)

    if logarithmic_y:
        ax.set_yscale("log")
        ax.yaxis.set_major_locator(LogLocator(base=10))
        ax.yaxis.set_major_formatter(LogFormatterMathtext())
    else:
        ax.set_ylim(bottom=0)

    ax.grid(
        True,
        which="major",
        color="#dddddd",
        linewidth=0.8,
    )
    ax.set_axisbelow(True)


def add_legend(ax):
    ax.legend(
        loc="upper left",
        bbox_to_anchor=(1.02, 1),
        borderaxespad=0,
        frameon=True,
        fontsize=9,
    )


def save_figure(fig, filename, layout_rect=None):
    if layout_rect is None:
        fig.tight_layout()
    else:
        fig.tight_layout(rect=layout_rect)

    path = PLOTS / filename
    fig.savefig(path, dpi=200, bbox_inches="tight")
    plt.close(fig)

    print(f"Saved: {path}")


def workload_series(rows, workload, variant=None):
    selected = [
        row for row in rows
        if row["workload"] == workload
        and (variant is None or row["variant"] == variant)
    ]

    for structure in ["DynamicArray", "MyLinkedList", "MinHeap"]:
        for current_variant in ["-", "head", "middle"]:
            data = sorted(
                [
                    row for row in selected
                    if row["structure"] == structure
                    and row["variant"] == current_variant
                ],
                key=lambda row: int(row["n"]),
            )

            if not data:
                continue

            label = structure

            if current_variant != "-":
                label += " / " + current_variant

            marker, linestyle = STYLES[current_variant]

            yield data, label, COLORS[structure], marker, linestyle


def plot_workload_time(rows, workload):
    fig, ax = plt.subplots(figsize=(10, 6))

    for data, label, color, marker, linestyle in workload_series(
        rows, workload
    ):
        ax.plot(
            [int(row["n"]) for row in data],
            [float(row["time_ms"]) for row in data],
            label=label,
            color=color,
            marker=marker,
            linestyle=linestyle,
            linewidth=1.8,
            markersize=6,
        )

    configure_axis(ax, "Time (ms)", logarithmic_y=True)
    ax.set_title("Time vs n")
    add_legend(ax)

    save_figure(fig, workload.lower() + "_time.png")


def plot_workload_metrics(rows, workload, variant, title):
    metrics = ["steps", "moves", "comparisons"]
    series = list(workload_series(rows, workload, variant))

    if not series:
        raise ValueError(f"No data for {workload}, {variant}")

    fig, axes = plt.subplots(1, 3, figsize=(15, 4.8))

    for ax, metric in zip(axes, metrics):
        all_values = []
        plotted_values = []

        for data, label, color, marker, linestyle in series:
            x_values = [int(row["n"]) for row in data]
            y_values = [int(row[metric]) for row in data]

            all_values.extend(y_values)
            plotted_values.append((x_values, y_values))

            # Distinct markers keep overlapping series visible.
            is_linked_list = data[0]["structure"] == "MyLinkedList"

            ax.plot(
                x_values,
                y_values,
                label=label,
                color=color,
                marker="x" if is_linked_list else marker,
                linestyle="--" if is_linked_list else linestyle,
                markerfacecolor="none",
                markeredgewidth=1.5,
                linewidth=1.8,
                markersize=6 if is_linked_list else 8,
                clip_on=False,
            )

        if any(value < 0 for value in all_values):
            raise ValueError("Operation counters cannot be negative")

        if all(value == 0 for value in all_values):
            configure_axis(ax, "Operation count")
            ax.set_ylim(-0.5, 0.5)
            ax.set_yticks([0])

            ax.text(
                0.5,
                0.7,
                "All values are zero",
                transform=ax.transAxes,
                ha="center",
                va="center",
                color="#555555",
            )

        elif all(value > 0 for value in all_values):
            configure_axis(
                ax,
                "Operation count",
                logarithmic_y=True,
            )

        else:
            # Handles possible future data containing both zero
            # and positive counters without dropping zero values.
            configure_axis(ax, "Operation count")
            ax.set_yscale("symlog", linthresh=1)
            ax.set_ylim(bottom=0)
            ax.set_ylabel("Operation count (symlog)")

        if (
            len(plotted_values) > 1
            and any(value > 0 for value in all_values)
            and all(
                values == plotted_values[0]
                for values in plotted_values[1:]
            )
        ):
            ax.text(
                0.03,
                0.95,
                "Series overlap exactly",
                transform=ax.transAxes,
                ha="left",
                va="top",
                fontsize=9,
                bbox={
                    "facecolor": "white",
                    "edgecolor": "none",
                    "alpha": 0.85,
                },
            )

        ax.set_title(metric.capitalize() + " vs n")

    handles, labels = axes[0].get_legend_handles_labels()

    fig.suptitle(title, y=0.99)

    fig.legend(
        handles,
        labels,
        loc="upper center",
        bbox_to_anchor=(0.5, 0.92),
        ncol=len(labels),
        frameon=True,
    )

    suffix = workload.lower()

    if variant != "-":
        suffix += "_" + variant

    save_figure(
        fig,
        suffix + "_metrics.png",
        layout_rect=(0, 0, 1, 0.82),
    )


def plot_heap_build(rows, input_type, metric, ylabel, title):
    fig, ax = plt.subplots(figsize=(10, 6))

    methods = [
        ("insert", "Repeated insertion", "tab:blue", "o", "-"),
        ("floyd", "Floyd buildHeap", "tab:orange", "s", "--"),
    ]

    for method, label, color, marker, linestyle in methods:
        data = sorted(
            [
                row for row in rows
                if row["input"] == input_type
                and row["method"] == method
            ],
            key=lambda row: int(row["n"]),
        )

        ax.plot(
            [int(row["n"]) for row in data],
            [float(row[metric]) for row in data],
            label=label + " / " + input_type,
            color=color,
            marker=marker,
            linestyle=linestyle,
            linewidth=1.8,
            markersize=6,
        )

    configure_axis(ax, ylabel, logarithmic_y=True)
    ax.set_title(title)
    add_legend(ax)

    suffix = "time" if metric == "time_ms" else "comparisons"

    save_figure(
        fig,
        f"bonus_heap_{input_type}_{suffix}.png",
    )


def plot_memory(rows):
    fig, ax = plt.subplots(figsize=(10, 6))

    styles = [
        ("DynamicArray", "o", "-", 8),
        ("MyLinkedList", "s", "-", 6),
        ("MinHeap", "x", "--", 6),
    ]

    for structure, marker, linestyle, marker_size in styles:
        data = sorted(
            [row for row in rows if row["structure"] == structure],
            key=lambda row: int(row["n"]),
        )

        ax.plot(
            [int(row["n"]) for row in data],
            [int(row["total_bytes"]) / 1024 for row in data],
            label=structure,
            color=COLORS[structure],
            marker=marker,
            markersize=marker_size,
            markerfacecolor="none",
            linestyle=linestyle,
            linewidth=1.8,
        )

    configure_axis(ax, "Memory (KiB)", logarithmic_y=True)
    ax.set_title("Memory vs n")
    add_legend(ax)

    save_figure(fig, "bonus_memory.png")


def main():
    PLOTS.mkdir(parents=True, exist_ok=True)

    workloads = read_csv("results.csv")
    heap_build = read_csv("heap_build.csv")
    memory = read_csv("memory.csv")

    for workload in ["W1", "W2", "W3", "W4"]:
        plot_workload_time(workloads, workload)

    metric_cases = [
        ("W1", "-", "W1: Indexed access"),
        ("W2", "-", "W2: Search"),
        ("W3", "head", "W3: Insert/remove at head"),
        ("W3", "middle", "W3: Insert/remove at middle"),
        ("W4", "-", "W4: Heap insertions and extractions"),
    ]

    for workload, variant, title in metric_cases:
        plot_workload_metrics(workloads, workload, variant, title)

    for input_type in ["random", "descending"]:
        plot_heap_build(
            heap_build,
            input_type,
            "time_ms",
            "Time (ms)",
            "Time vs n",
        )

        plot_heap_build(
            heap_build,
            input_type,
            "comparisons",
            "Value comparisons",
            "Comparisons vs n",
        )

    plot_memory(memory)

    print("Done: 14 figures generated.")


if __name__ == "__main__":
    main()
