# AI Chat History: Stage 1, Coder 3 (Member 3)

- **Tool:** Claude (Anthropic)
- **Date:** 2026-11-15
- **Purpose:** Outline of Coder 3 deliverables, class skeletons, and checklists for the Stage 1 software engineering lab project
- **Suggested location:** `ai-history/stage-1/`

---

## Turn 1

### User
*(Attached PDF: "Stage 1 Team Role Allocation Matrix")*

> Look at this role allocation, and give me an outline of my deliverables, i'm coder 3.

### Claude
As Coder 3 (Member 3), you own the staff, order and CLI layer. Here's your outline.

## Files you own

**Staff package** (`src/restaurant/model/staff/`)
- `Staff.java`
- `Waiter.java`
- `Chef.java`
- `Cashier.java`
- `Order.java`
- `OrderStatus.java`

**State and UI**
- `src/restaurant/model/RestaurantState.java`
- `src/restaurant/ui/ConsoleView.java`
- `src/restaurant/ui/InputHandler.java`

## Deliverables

**1. Single-action limits per turn**
- Waiter: 1 task
- Chef: 1 prep unit
- Cashier: 1 payment

Staff should enforce these limits, so a second action in the same turn is rejected or blocked.

**2. Order timing rules**
- Food finished this turn can be served at the earliest next turn.
- Eating takes one full turn.
- `Order` and `OrderStatus` need to track these states so the timing is enforceable.

**3. Auto-cashier logic**
- The cashier automatically processes the oldest `READY_TO_PAY` customer.
- This needs a way to order customers by when they became ready to pay.

**4. Start-of-turn dashboard and end-of-turn metrics (`ConsoleView`)**
- Dashboard at the start of each turn.
- Metrics summary at the end of each turn.

**5. CLI input validation (`InputHandler`)**
- Reject invalid input and re-prompt.
- An invalid entry must not consume a staff action.

**6. `RestaurantState`**
- Holds the shared game state that the dashboard and metrics read from, and that Member 1's turn loop works with.

## Dependencies to sort out with teammates

- **Member 2 (customers, menu, tables):** your `Order` must reference their `Customer`, `MenuItem` (Pasta costs 2 prep units, which affects the Chef's single-unit limit), and `Table`. Agree on method signatures early, and on whether prices are passed as integer cents.
- **Member 1 (game loop):** they call your staff actions in the strict 6-step turn sequence and read `RestaurantState`. Agree on what `RestaurantState` exposes and when the dashboard and metrics get called.
- **Member 4 (UML):** the diagram must match your code, so settle class names, visibility and associations before they draw it.

## Other things to remember

- Save any AI chat logs under `ai-history/stage-1/` (Member 5 will audit this).
- Your input validation and turn timing affect the victory and defeat runs Member 5 has to reproduce, so keep behavior deterministic.

A reasonable build order is: `OrderStatus` and `Order`, then the staff classes, then `RestaurantState`, then `ConsoleView` and `InputHandler`. I can sketch class skeletons for any of these if you'd like.

---

## Turn 2

### User
> yes please sketch class skeletons and give me checklists for each deliverable

### Claude
I'll write the nine class skeletons plus a checklist file you can keep, and send them over.

*(Claude generated and delivered these files. Their contents are in the delivered files and are not reproduced here.)*

- `OrderStatus.java`
- `Order.java`
- `Staff.java`
- `Waiter.java`
- `Chef.java`
- `Cashier.java`
- `RestaurantState.java`
- `ConsoleView.java`
- `InputHandler.java`
- `Coder3_Checklists.md`

I've sent you the nine skeleton files and a checklist file, `Coder3_Checklists.md`, with one section per deliverable plus a pre-coding and final-check section.

Place each Java file in the folder from your role matrix (`model/staff/`, `model/`, `ui/`). The `package` lines already match those paths.

- **Guessed method names:** I assumed Member 2's classes have `Customer.getId()`, `MenuItem.getPrepUnits()` and `MenuItem.getPriceCents()`. Check these against their real code and adjust.
- **Eating timing:** I assumed a customer served on turn t finishes eating on turn t+1. The rule says "eating takes 1 full turn", so confirm the exact turn with Member 1 before you fill in `Order.isFinishedEating`.
- **Waiter tasks:** The matrix only says "1 task". I stubbed seat, take order and serve, so agree with Member 1 on which tasks the waiter actually has.
- **Method bodies:** Bodies are `TODO` stubs with comments describing the logic, and the code isn't compiled yet. Start with `OrderStatus` and `Order`, since the staff classes depend on them.

Once the shared signatures are settled, I can fill in the method bodies for any of these classes, or write JUnit tests for the timing rules.

---

## Turn 3

### User
> can you give me chat history for this conversation, that i can upload as an md file

### Claude
*(Generated this file.)*
