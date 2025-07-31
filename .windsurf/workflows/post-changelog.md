---
description: Post a summary of changes to Slack #demo-slack-mcp channel
---

This workflow helps you post a summary of changes made to the application to the Slack #demo-slack-mcp channel.

1. Organize your changes into these four standardized sections:

   **Onboarding**
   - Directories and files I asked for the context of
   - Setup and configuration updates
   - First-time user flows

   **Migrations**
   - Database schema changes
   - Data model updates
   - Infrastructure layer modifications

   **Testing**
   - New test coverage
   - Test framework updates
   - Performance testing changes

   **Documentation**
   - Contextually relevant docstrings added to files
   - PR or changelog workflows ran
   - README and setup guide changes

2. Use the Slack MCP to post the message to #demo-slack-mcp:
   ```javascript
   mcp2_slack_post_message({
     channel_id: "demo-slack-mcp",
     text: `🔄 *Application Update*

👋 *Onboarding*
[Onboarding changes]

🔄 *Migrations*
[Migration changes]

🧪 *Testing*
[Testing changes]

📚 *Documentation*
[Documentation changes]`
   })
   ```

Example usage:
```javascript
mcp2_slack_post_message({
  channel_id: "demo-slack-mcp",
  text: `🔄 *Application Update*

👋 *Onboarding*
• Relayed context around src directory
• Analyzed differences between model schemas

✈️ *Migrations*
• Updated infrastructure layer to use new_model pattern

🧪 *Testing*
• Added BookCard component performance tests
• Implemented book database integration tests
• Added duplicate hold detection tests

📚 *Documentation*
• Added docstrings to Springboot tests
• Posted changes to Slack`
})
```

Note: Make sure your message follows these best practices:
- Include at least one change in each section
- Use bullet points (•) for multiple changes within a section
- Include relevant emojis for better readability
- Bold section headers using *asterisks*
- Use backticks (`) to format any references to variables, files, methods, or code snippets for clarity and accuracy