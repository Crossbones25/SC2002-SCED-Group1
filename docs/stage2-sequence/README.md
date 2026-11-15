Stage 2 Sequence Diagram — Critic Priority

Use Case: Managing Competing Waiter Tasks During Critic Service

This sequence diagram illustrates the Manager's decision during Turn 14 of Stage 2, where Critic Customer #4 has a READY Pasta order at Table 2, while Regular Customer #5 is waiting to order at Table 1.

The Manager can choose to serve the Critic, preserving their satisfaction at 52, or take the Regular's order, causing the Critic's satisfaction to decrease to 40.

The use case is represented using one main sequence diagram and two referenced interactions.

Diagram Files

- [Complete Sequence Diagram Overview](Stage2_SequenceOverview.png)
- [Main Sequence Diagram — Stage2_CriticPriority](Stage2_CriticPriority.png)
- [Referenced Interaction — PrepareCriticOrder (Turns 10–13)](PrepareCriticOrder.png)
- [Referenced Interaction — CompleteCriticService (Turns 15–18)](CompleteCriticService.png)

The diagrams use UML `alt`, `opt`, `loop` and `ref` fragments to represent managerial decisions, object interactions and game progression.

The corresponding use case description and design explanation are included in Section 5 of the group report.
