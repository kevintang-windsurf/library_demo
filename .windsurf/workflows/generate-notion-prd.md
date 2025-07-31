---
description: Generate a PRD in Notion
---

# Generate Notion PRD Workflow

## Description
This workflow analyzes a specified directory or file in the codebase and generates a comprehensive Product Requirements Document (PRD) in Notion with a consistent format.

## Usage
Run this workflow from the Windsurf editor using:
```
/workflow generate-notion-prd
```

## Parameters
- `target_path`: The path to analyze (can be a directory, subdirectory, or single file)
- `notion_page_title`: Title for the Notion page (optional, will auto-generate if not provided)

## Workflow Steps

### Step 1: Analyze Target Path
```javascript
// Determine if target is file or directory
const fs = require('fs');
const path = require('path');

const targetPath = params.target_path || process.cwd();
const isDirectory = fs.statSync(targetPath).isDirectory();
const baseName = path.basename(targetPath);
```

### Step 2: Code Analysis
```javascript
// Analyze code structure, dependencies, and patterns
const analysis = {
    fileTypes: [],
    dependencies: [],
    architecture: '',
    patterns: [],
    complexity: '',
    testCoverage: '',
    documentation: []
};

// Scan for different file types and technologies
// Extract dependencies from build files (pom.xml, package.json, etc.)
// Identify architectural patterns (DDD, hexagonal, etc.)
// Analyze test files and coverage
```

### Step 3: Generate PRD Content
```markdown
# Product Requirements Document: {{PROJECT_NAME}}

## 📋 Document Information
- **Created**: {{CURRENT_DATE}}
- **Author**: {{AUTHOR_NAME}}
- **Version**: 1.0
- **Target Path**: {{TARGET_PATH}}
- **Analysis Scope**: {{SCOPE_DESCRIPTION}}

## 🎯 Executive Summary
{{EXECUTIVE_SUMMARY}}

## 🏗️ Technical Overview

### Architecture
- **Pattern**: {{ARCHITECTURE_PATTERN}}
- **Language(s)**: {{PROGRAMMING_LANGUAGES}}
- **Framework(s)**: {{FRAMEWORKS}}
- **Build System**: {{BUILD_SYSTEM}}

### Key Components
{{COMPONENT_LIST}}

## 📊 Project Analysis

### Code Structure
```
{{DIRECTORY_TREE}}
```

### Dependencies
{{DEPENDENCY_ANALYSIS}}

### Design Patterns
{{DESIGN_PATTERNS}}

## 🔧 Technical Requirements

### Functional Requirements
{{FUNCTIONAL_REQUIREMENTS}}

### Non-Functional Requirements
{{NON_FUNCTIONAL_REQUIREMENTS}}

### System Requirements
{{SYSTEM_REQUIREMENTS}}

## 🧪 Testing Strategy

### Test Coverage
{{TEST_COVERAGE_ANALYSIS}}

### Test Types
{{TEST_TYPES}}

### Quality Metrics
{{QUALITY_METRICS}}

## 📚 Domain Model

### Business Logic
{{BUSINESS_LOGIC_DESCRIPTION}}

### Domain Entities
{{DOMAIN_ENTITIES}}

### Business Rules
{{BUSINESS_RULES}}

## 🔄 Integration Points

### External Dependencies
{{EXTERNAL_DEPENDENCIES}}

### APIs and Interfaces
{{API_INTERFACES}}

### Data Flow
{{DATA_FLOW_DESCRIPTION}}

## 🚀 Implementation Details

### Key Classes/Modules
{{KEY_IMPLEMENTATIONS}}

### Configuration
{{CONFIGURATION_DETAILS}}

### Deployment
{{DEPLOYMENT_STRATEGY}}

## 📈 Metrics and Monitoring

### Performance Metrics
{{PERFORMANCE_METRICS}}

### Monitoring Strategy
{{MONITORING_STRATEGY}}

### Logging
{{LOGGING_STRATEGY}}

## 🔒 Security Considerations
{{SECURITY_ANALYSIS}}

## 📋 Acceptance Criteria
{{ACCEPTANCE_CRITERIA}}

## 🗺️ Future Roadmap
{{FUTURE_ENHANCEMENTS}}

## 📎 Appendices

### Code Samples
{{REPRESENTATIVE_CODE_SAMPLES}}

### Configuration Files
{{CONFIG_FILE_SAMPLES}}

### Documentation Links
{{DOCUMENTATION_REFERENCES}}
```

### Step 4: Create Notion Page
```javascript
// Use Notion API to create the PRD page
const { Client } = require('@notionhq/client');

const notion = new Client({
    auth: process.env.NOTION_TOKEN,
});

const response = await notion.pages.create({
    parent: {
        database_id: process.env.NOTION_DATABASE_ID,
    },
    properties: {
        title: {
            title: [
                {
                    text: {
                        content: pageTitle,
                    },
                },
            ],
        },
        Status: {
            select: {
                name: "Draft",
            },
        },
        Type: {
            select: {
                name: "PRD",
            },
        },
        Created: {
            date: {
                start: new Date().toISOString(),
            },
        },
    },
    children: [
        // Convert markdown content to Notion blocks
        ...convertMarkdownToNotionBlocks(prdContent),
    ],
});
```

## Configuration

### Environment Variables Required
```bash
# Notion Integration
NOTION_TOKEN=your_notion_integration_token
NOTION_DATABASE_ID=your_database_id

# Optional: Custom settings
PRD_TEMPLATE_VERSION=1.0
AUTHOR_NAME=your_name
ANALYSIS_DEPTH=detailed # or basic
```

### Notion Database Schema
Create a Notion database with these properties:
- **Title** (Title)
- **Status** (Select: Draft, Review, Approved, Archived)
- **Type** (Select: PRD, Technical Spec, Analysis)
- **Created** (Date)
- **Last Updated** (Date)
- **Author** (Person)
- **Target Path** (Text)
- **Complexity** (Select: Low, Medium, High)
- **Priority** (Select: Low, Medium, High, Critical)

## Analysis Templates

### For Domain-Driven Design Projects
```javascript
const dddAnalysis = {
    boundedContexts: extractBoundedContexts(targetPath),
    aggregates: identifyAggregates(targetPath),
    domainEvents: findDomainEvents(targetPath),
    repositories: locateRepositories(targetPath),
    services: identifyDomainServices(targetPath),
    valueObjects: findValueObjects(targetPath),
};
```

### For Microservices Architecture
```javascript
const microserviceAnalysis = {
    services: identifyServices(targetPath),
    apis: extractApiDefinitions(targetPath),
    databases: identifyDataStores(targetPath),
    messaging: findMessageQueues(targetPath),
    configuration: extractConfigFiles(targetPath),
};
```

### For Frontend Applications
```javascript
const frontendAnalysis = {
    components: identifyComponents(targetPath),
    routes: extractRoutes(targetPath),
    stateManagement: identifyStatePattern(targetPath),
    styling: analyzeStyleSystem(targetPath),
    bundling: analyzeBuildSystem(targetPath),
};
```

## Output Format

The generated PRD will be created in Notion with:
- Consistent formatting and structure
- Embedded code samples with syntax highlighting
- Interactive elements (checkboxes, toggles)
- Linked references between sections
- Automated table of contents
- Tags and metadata for easy filtering

## Error Handling

```javascript
try {
    // Workflow execution
} catch (error) {
    console.error('PRD Generation Failed:', error);
    
    // Create fallback documentation
    const fallbackPRD = generateBasicAnalysis(targetPath);
    
    // Log to Notion with error status
    await createErrorReport(error, targetPath);
}
```

## Success Criteria

✅ PRD is created in Notion with all required sections
✅ Code analysis is comprehensive and accurate
✅ Technical details are properly documented
✅ Business context is clearly explained
✅ Future roadmap is outlined
✅ All code samples are properly formatted
✅ Links and references are working

## Notes

- This workflow requires Notion API access and proper authentication
- Large codebases may take several minutes to analyze
- The analysis depth can be configured based on needs
- Generated PRDs follow a consistent template for easy comparison
- All sensitive information is automatically redacted from the output
