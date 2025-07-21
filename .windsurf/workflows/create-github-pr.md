---
description: Create a GitHub Pull Request for library_demo
---

This workflow creates a new Pull Request in the ben-windsurf/library_demo repository.

1. Create a new branch for your changes:
```bash
git checkout -b <branch-name>
```

2. Stage your changes:
```bash
git add .
```

3. Create a commit with a descriptive message:
```bash
git commit -m "<commit-message>"
```

4. Push the branch to GitHub:
```bash
git push origin <branch-name>
```

5. Create the Pull Request using GitHub MCP:
   - The MCP will automatically create a PR from your branch to the main branch
   - It will use your commit message as the PR description
   - You can add additional context or labels as needed

Note: Replace `<branch-name>` with a descriptive name for your feature/changes and `<commit-message>` with a clear description of your changes.