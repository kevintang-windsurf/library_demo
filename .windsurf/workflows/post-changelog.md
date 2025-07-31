---
description: Post a summary of changes to Slack #change-log-demo channel
---

This workflow helps you post a summary of changes made to the application to the Slack #change-log-demo channel.

1. First, write a brief summary of the changes you want to post. Keep it concise but informative. Include:
   - What was changed
   - Why it was changed
   - Any important technical details

2. Use the Slack MCP to post the message to #change-log-demo:
   ```javascript
   mcp1_slack_post_message({
     channel_id: "change-log-demo",
     text: "🔄 *Application Update*\n\n[Your change summary here]"
   })
   ```

Example usage:
```javascript
mcp1_slack_post_message({
  channel_id: "change-log-demo",
  text: "🔄 *Application Update*\n\n• Added new book checkout validation\n• Implemented rate limiting for API endpoints\n• Fixed bug in patron registration flow"
})
```

Note: Make sure your message follows these best practices:
- Use bullet points for multiple changes
- Start with most important changes
- Include emojis for better readability
- Bold important sections using *asterisks*
- Mention any breaking changes prominently
