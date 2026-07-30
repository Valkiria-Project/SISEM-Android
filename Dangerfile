message "Thanks @#{github.pr_author}!"

if github.pr_body.length == 0
    fail "Please provide a summary in the Pull Request description."
end

if git.lines_of_code > 500
    warn "Please consider breaking up this pull request."
end

if github.pr_labels.empty?
    warn "Please add labels to this PR."
end

if git.deletions > git.insertions
    message  "🎉 Code Cleanup!"
end

# Remind to record user-facing changes in the changelog.
#
# Only shipped code counts: a PR that just moves tests, docs or build configuration has
# nothing for a release note. The changelog lives here and is mirrored to GitLab, so it
# has to be edited in this repository — edits made on the mirror are overwritten by the
# next sync.
touchedShippedCode = (git.modified_files + git.added_files).any? do |file|
    file.start_with?("app/src/main/", "uicomponents/src/main/")
end

if touchedShippedCode && !(git.modified_files + git.added_files).include?("CHANGELOG.md")
    warn "This PR changes shipped code but does not update CHANGELOG.md. " \
         "Add an entry under the target version, or say in the description why it is not needed."
end

# Notify of outdated dependencies
dependencyUpdatesHeader = "The following dependencies have later milestone versions:"
dependencyReportsFile = "app/build/dependencyUpdates/report.txt"

# Due to the formatting of this output file, we first check if there are any dependencies
# by looking for a `->` arrow, then we check for the relevant headers. We do this to handle a case
# where there are no app dependencies but there are Gradle dependencies.
hasDependencyUpdatesHeader = File.readlines(dependencyReportsFile).grep(/#{dependencyUpdatesHeader}/).any?

if hasDependencyUpdatesHeader
  file = File.open(dependencyReportsFile, "rb").read
  index = file.index(dependencyUpdatesHeader)
  message file.slice(index..-1)
end