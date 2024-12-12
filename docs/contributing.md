# Contributing to Interactive Floor System

First off, thank you for considering contributing to Interactive Floor System! It's people like you that make this project better.

## Table of Contents
1. [Code of Conduct](#code-of-conduct)
2. [Getting Started](#getting-started)
3. [How Can I Contribute?](#how-can-i-contribute)
4. [Development Process](#development-process)
5. [Pull Request Guidelines](#pull-request-guidelines)
6. [Style Guidelines](#style-guidelines)
7. [Commit Messages](#commit-messages)
8. [Documentation](#documentation)

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code. Please report unacceptable behavior to [hakmesyo@gmail.com](mailto:hakmesyo@gmail.com).

### Our Standards
- Using welcoming and inclusive language
- Being respectful of differing viewpoints and experiences
- Gracefully accepting constructive criticism
- Focusing on what is best for the community
- Showing empathy towards other community members

## Getting Started

1. Fork the repository
2. Clone your fork:
```bash
git clone https://github.com/your-username/interactive-floor.git
```
3. Add the upstream repository:
```bash
git remote add upstream https://github.com/hakmesyo/interactive-floor.git
```
4. Create a branch for your changes:
```bash
git checkout -b feature/your-feature-name
```

## How Can I Contribute?

### Reporting Bugs
- Use the GitHub issue tracker
- Check if the issue already exists
- Include detailed steps to reproduce
- Provide system information
- Include relevant logs and screenshots

### Suggesting Enhancements
- Use the GitHub issue tracker
- Tag your issue as 'enhancement'
- Provide a clear use case
- Explain the expected benefit
- Include mock-ups if applicable

### Code Contributions
1. New Animations
2. Performance Improvements
3. Bug Fixes
4. Documentation
5. Tests

## Development Process

1. **Branch Names**
   - Features: `feature/description`
   - Bugs: `fix/description`
   - Documentation: `docs/description`

2. **Testing**
   - Add tests for new features
   - Ensure all tests pass
   - Update relevant documentation

3. **Code Review**
   - All changes require review
   - Address review comments
   - Keep discussions professional

## Pull Request Guidelines

### Before Submitting
1. Update your fork
```bash
git fetch upstream
git rebase upstream/main
```

2. Run tests
```bash
mvn test
```

3. Check code style
```bash
mvn checkstyle:check
```

### PR Template
```markdown
## Description
[Description of your changes]

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Documentation update
- [ ] Performance improvement

## Test Plan
[How did you test your changes?]

## Screenshots (if applicable)
[Add screenshots]
```

## Style Guidelines

### Java Code Style
- Use 4 spaces for indentation
- Maximum line length: 100 characters
- Follow Java naming conventions
- Document public APIs
- Include unit tests

Example:
```java
public class CustomAnimation implements Animation {
    private static final int MAX_PARTICLES = 1000;
    
    @Override
    public void update(PApplet app, Player player) {
        // Implementation
    }
}
```

### Documentation Style
- Use Markdown for documentation
- Include code examples
- Keep language clear and concise
- Update relevant diagrams

## Commit Messages

### Format
```
type(scope): Subject

[optional body]

[optional footer]
```

### Types
- feat: New feature
- fix: Bug fix
- docs: Documentation
- style: Formatting
- refactor: Code restructuring
- test: Adding tests
- chore: Maintenance

Example:
```
feat(animation): Add new particle system

- Implemented particle pooling
- Added color transitions
- Optimized rendering

Fixes #123
```

## Documentation

### Required Documentation
1. Code Comments
   - Public API documentation
   - Complex algorithm explanations
   - Known limitations

2. README Updates
   - New features
   - Changed behavior
   - Updated requirements

3. API Documentation
   - Method signatures
   - Parameter descriptions
   - Return values
   - Examples

### Example Documentation
```java
/**
 * Creates a new animation effect.
 *
 * @param intensity Effect intensity (0.0 to 1.0)
 * @param color Base color for the effect
 * @return The created effect instance
 * @throws IllegalArgumentException if intensity is out of range
 */
public Effect createEffect(float intensity, int color) {
    // Implementation
}
```

## Getting Help

- Join our [Discord server](https://discord.gg/your-invite)
- Check the [FAQ](docs/FAQ.md)
- Ask in GitHub Issues
- Email: hakmesyo@gmail.com

## Recognition

Contributors will be:
- Listed in CONTRIBUTORS.md
- Mentioned in release notes
- Credited in documentation

Thank you for contributing to Interactive Floor System!
