# Ford-Fulkerson Algorithm: Residual Graphs Analysis

## Project Overview
This project focuses on analyzing different algorithms used to find augmenting paths in the Ford-Fulkerson algorithm, a well-known approach to compute the maximum flow in a flow network. The goal is to explore various augmenting path algorithms and evaluate their performance in terms of paths, mean lengths, and proportional lengths on randomly generated source-sink graphs.

## Key Algorithms Explored:
- **Shortest Augmenting Path (SAP)**
- **DFS-Like**
- **Maximum Capacity (MaxCap)**
- **Random**

## Project Objectives:
- **Analyze augmenting paths**: Implement and compare the performance of four augmenting path algorithms using multiple metrics.
- **Perform simulations**: Evaluate the performance of these algorithms on different graph configurations.
- **Implementation correctness**: Test for accuracy and compare results across simulations.

## Table of Contents
1. [Problem Description](#problem-description)
2. [Implementation Details](#implementation-details)
3. [Correctness Verification](#correctness-verification)
4. [Results](#results)
5. [Conclusion](#conclusion)

## Problem Description
The project implements four augmenting path algorithms in the context of the Ford-Fulkerson framework. Each algorithm is evaluated for:
- **Number of augmenting paths** needed until maximum flow is achieved.
- **Mean length (ML)**: Average path length in terms of edges.
- **Mean Proportional Length (MPL)**: Path length as a fraction of the longest acyclic path.
- **Total edges** in the graph.

Two simulations were conducted with various graph parameters (`n`, `r`, `upperCap`) to explore algorithm performance under different conditions.

## Implementation Details
The project consists of the following key steps:
1. **Graph Generation**: Random source-sink graphs generated using adjacency matrices.
2. **CSV Data Storage**: Store and retrieve graph and vertex information using CSV files.
3. **Algorithm Implementation**: Code implementations for SAP, DFS-Like, MaxCap, and Random algorithms.
4. **Ford-Fulkerson Integration**: All algorithms were applied within the Ford-Fulkerson framework to compute maximum flow.

## Correctness Verification
The algorithms were validated through:
- Testing with small graphs for obvious results.
- Verifying that all augmenting path algorithms yield the same maximum flow.

## Results
The project includes results for two simulation phases:
1. **Simulation 1**: Performance comparison of the algorithms with predefined configurations.
2. **Simulation 2**: Custom configurations to highlight the behavior of each algorithm.

Key findings:
- **Shortest Augmenting Path** consistently required fewer paths and had shorter mean lengths.
- **DFS-Like** and **MaxCap** showed increased path lengths for larger networks and higher edge densities.

For detailed results, please refer to the report.

## Conclusion
The performance of the augmenting path algorithms varies based on the network size, edge density, and capacity. For small networks or low edge density, the differences are minor, but as the network grows, the algorithms exhibit distinct behavior in terms of path length and number of edges.
