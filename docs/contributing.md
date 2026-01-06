# :herb: Everdale - Contributing

Welcome — thank you for considering contributing to Everdale! This document explains how to report issues, request features, and contribute code or assets (art, audio, etc.) in a way that helps maintainers review and merge your contributions quickly.

## How to contribute

1. Opening an issue

   - Use issues to report bugs, suggest enhancements, or ask for help.
   - When opening a bug report include:
     - A short, descriptive title
     - A clear description of the problem or requested feature
     - Steps to reproduce (if applicable)
     - Expected vs actual behavior
     - Relevant logs or crash reports (attach files or paste snippets)
     - Your environment: Everdale version (commit/branch), Java/Forge/Fabric version (if relevant), OS

2. Working on code

   - Fork the repository and create a branch named descriptively (e.g., `feature/save-load` or `fix/null-pointer-when-loading`).
   - Keep pull requests focused: one PR = one logical change.
   - Include tests if the change affects logic or behavior.
   - Follow existing code style and conventions. If unsure, mirror nearby code or ask in an issue first.
   - Provide a clear PR description that explains the problem, solution, and any migration or compatibility notes.

3. Contributing assets (art, audio, models)

   - Prefer common, open formats (PNG/WebP for images, OGG/FLAC for audio, GLTF/OBJ for models) unless the project requires a specific format.
   - Keep file sizes reasonable and include source files when possible (e.g., PSD, .blend) so maintainers can edit.
   - Add a short README or a comment explaining where and how the asset should be used.
   - Ensure you have the rights to contribute the asset and license it appropriately (see Licensing below).

4. Creating pull requests

   - Push your branch to your fork and open a PR against `main` (or the target branch specified in the issue).
   - Link the related issue using keywords if the PR closes an issue (e.g., `Closes #123`).
   - Keep the PR description focused and include screenshots or samples for asset changes.
   - Be responsive to review feedback — maintainers may request changes before merging.

5. Review process and expectations

   - Maintainers will review PRs and may ask for changes, tests, or clarifications.
   - Small or urgent fixes may be merged faster; larger changes may take longer to review.
   - If your PR needs rebasing or has merge conflicts, maintainers may ask you to update the branch.

6. Licensing and ownership

   - By contributing, you agree to license your contributions under this repository's license. If you are submitting third-party assets, ensure they are compatible with the project license and include attribution where required.
   - If you cannot grant the required license for a contribution, do not submit it.

7. Sensitive files and secrets

   - Do not commit secrets, private keys, or credentials (keystore files, API keys, `local.properties`, `.env`, etc.). If you accidentally commit secrets, contact the maintainers immediately and remove them from history.

8. Code of conduct

   - Be respectful and constructive. If a Code of Conduct exists in the repo, follow it; otherwise follow common open source community standards.

Thank you — contributions make Everdale better. If you have any questions about where to start, open an issue titled "Help wanted: where to contribute" and we will suggest good first issues.
