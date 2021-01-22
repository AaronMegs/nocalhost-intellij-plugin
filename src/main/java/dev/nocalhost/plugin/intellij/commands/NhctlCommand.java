package dev.nocalhost.plugin.intellij.commands;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import dev.nocalhost.plugin.intellij.commands.data.NhctlConfigOptions;
import dev.nocalhost.plugin.intellij.commands.data.NhctlDescribeOptions;
import dev.nocalhost.plugin.intellij.commands.data.NhctlDescribeResult;
import dev.nocalhost.plugin.intellij.commands.data.NhctlDevEndOptions;
import dev.nocalhost.plugin.intellij.commands.data.NhctlDevStartOptions;
import dev.nocalhost.plugin.intellij.commands.data.NhctlGlobalOptions;
import dev.nocalhost.plugin.intellij.commands.data.NhctlInstallOptions;
import dev.nocalhost.plugin.intellij.commands.data.NhctlPluginOptions;
import dev.nocalhost.plugin.intellij.commands.data.NhctlPortForwardOptions;
import dev.nocalhost.plugin.intellij.commands.data.NhctlSyncOptions;
import dev.nocalhost.plugin.intellij.commands.data.NhctlUninstallOptions;


public final class NhctlCommand {
    private static final String NHCTL_COMMAND = "nhctl";
    private final Yaml yaml;

    public NhctlCommand() {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        this.yaml = new Yaml(representer);
    }

    public void install(String name, NhctlInstallOptions opts) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "install", name);
        if (StringUtils.isNotEmpty(opts.getConfig())) {
            args.add("--config");
            args.add(opts.getConfig());
        }
        if (StringUtils.isNotEmpty(opts.getGitRef())) {
            args.add("--git-ref");
            args.add(opts.getGitRef());
        }
        if (StringUtils.isNotEmpty(opts.getGitUrl())) {
            args.add("--git-url");
            args.add(opts.getGitUrl());
        }
        if (StringUtils.isNotEmpty(opts.getHelmChartName())) {
            args.add("--helm-chart-name");
            args.add(opts.getHelmChartName());
        }
        if (StringUtils.isNotEmpty(opts.getHelmRepoName())) {
            args.add("--helm-repo-name");
            args.add(opts.getHelmRepoName());
        }
        if (StringUtils.isNotEmpty(opts.getHelmRepoUrl())) {
            args.add("--helm-repo-url");
            args.add(opts.getHelmRepoUrl());
        }
        if (StringUtils.isNotEmpty(opts.getHelmRepoVersion())) {
            args.add("--helm-repo-version");
            args.add(opts.getHelmRepoVersion());
        }
        if (StringUtils.isNotEmpty(opts.getHelmValues())) {
            args.add("--helm-values");
            args.add(opts.getHelmValues());
        }
        if (opts.isIgnorePreInstall()) {
            args.add("--ignore-pre-install");
        }
        if (StringUtils.isNotEmpty(opts.getNamespace())) {
            args.add("--namespace");
            args.add(opts.getNamespace());
        }
        if (StringUtils.isNotEmpty(opts.getOuterConfig())) {
            args.add("--outer-config");
            args.add(opts.getOuterConfig());
        }
        if (opts.getResourcesPath() != null) {
            for (String path : opts.getResourcesPath()) {
                args.add("--resource-path");
                args.add(path);
            }
        }
        if (opts.getValues() != null) {
            String values = opts.getValues().entrySet()
                    .stream()
                    .map((e) -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.toList())
                    .stream()
                    .reduce(",", String::concat);
            if (StringUtils.isNotEmpty(values)) {
                args.add("--set");
                args.add(values);
            }
        }
        if (StringUtils.isNotEmpty(opts.getType())) {
            args.add("--type");
            args.add(opts.getType());
        }
        if (opts.isWait()) {
            args.add("--wait");
        }

        execute(args, opts);
    }

    public void uninstall(String name, NhctlUninstallOptions opts) throws InterruptedException, IOException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "uninstall", name);
        if (opts.isForce()) {
            args.add("--force");
        }

        execute(args, opts);
    }

    public void devStart(String name, NhctlDevStartOptions opts) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "dev", "start", name);
        if (StringUtils.isNotEmpty(opts.getDeployment())) {
            args.add("--deployment");
            args.add(opts.getDeployment());
        }
        if (StringUtils.isNotEmpty(opts.getImage())) {
            args.add("--image");
            args.add(opts.getImage());
        }
        if (opts.getLocalSync() != null) {
            for (String s : opts.getLocalSync()) {
                args.add("--local-sync");
                args.add(s);
            }
        }
        if (StringUtils.isNotEmpty(opts.getSidecarImage())) {
            args.add("--sidecar-image");
            args.add(opts.getSidecarImage());
        }
        if (StringUtils.isNotEmpty(opts.getStorageClass())) {
            args.add("--storage-class");
            args.add(opts.getStorageClass());
        }
        if (StringUtils.isNotEmpty(opts.getSyncthingVersion())) {
            args.add("--syncthing-version");
            args.add(opts.getSyncthingVersion());
        }
        if (StringUtils.isNotEmpty(opts.getWorkDir())) {
            args.add("--work-dir");
            args.add(opts.getWorkDir());
        }

        execute(args, opts);
    }

    public void devEnd(String name, NhctlDevEndOptions opts) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "dev", "end", name);
        if (StringUtils.isNotEmpty(opts.getDeployment())) {
            args.add("--deployment");
            args.add(opts.getDeployment());
        }

        execute(args, opts);
    }

    public void sync(String name, NhctlSyncOptions opts) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "sync", name);
        if (opts.isDaemon()) {
            args.add("--daemon");
        }
        if (StringUtils.isNotEmpty(opts.getDeployment())) {
            args.add("--deployment");
            args.add(opts.getDeployment());
        }
        if (opts.isDoubleSideSync()) {
            args.add("--double");
        }
        if (opts.getIgnoredPatterns() != null) {
            for (String pattern : opts.getIgnoredPatterns()) {
                args.add("--ignored-pattern");
                args.add(pattern);
            }
        }
        if (opts.getSyncedPatterns() != null) {
            for (String pattern : opts.getSyncedPatterns()) {
                args.add("--synced-pattern");
                args.add(pattern);
            }
        }

        execute(args, opts);
    }

    public void portForward(String name, NhctlPortForwardOptions opts) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "port-forward", name);
        if (opts.isDaemon()) {
            args.add("--daemon");
        }
        if (StringUtils.isNotEmpty(opts.getDeployment())) {
            args.add("--deployment");
            args.add(opts.getDeployment());
        }
        if (opts.getDevPorts() != null) {
            for (String port : opts.getDevPorts()) {
                args.add("--dev-port");
                args.add(port);
            }
        }

        execute(args, opts);
    }

    public NhctlDescribeResult describe(String name, NhctlDescribeOptions opts) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "describe", name);
        if (StringUtils.isNotEmpty(opts.getDeployment())) {
            args.add("--deployment");
            args.add(opts.getDeployment());
        }
        String result = execute(args, opts);
        return yaml.loadAs(result, NhctlDescribeResult.class);
    }

    public void reset(String name, NhctlResetOptions opts) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "dev", "reset", name);
        if (StringUtils.isNotEmpty(opts.getDeployment())) {
            args.add("--deployment");
            args.add(opts.getDeployment());
        }
        execute(args, opts);
    }

    public String getConfig(String name, NhctlConfigOptions opts) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "config", "get", name);
        if (StringUtils.isNotEmpty(opts.getDeployment())) {
            args.add("--deployment");
            args.add(opts.getDeployment());
        }
        addGlobalOptions(args, opts);

        return execute(args, opts);
    }

    public void getTemplateConfig(String name, NhctlConfigOptions opts) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "config", "template", name);
        if (StringUtils.isNotEmpty(opts.getDeployment())) {
            args.add("--deployment");
            args.add(opts.getDeployment());
        }
        execute(args, opts);
    }

    public void saveConfig(String name, NhctlConfigOptions opts, String content) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "config", "get", name);
        if (StringUtils.isNotEmpty(opts.getDeployment())) {
            args.add("--deployment");
            args.add(opts.getDeployment());
        }
        args.add("--content");
        args.add(content);
        execute(args, opts);
    }

    public void getPluginInfo(String name, NhctlPluginOptions opts) throws IOException, InterruptedException {
        List<String> args = Lists.newArrayList(NHCTL_COMMAND, "plugin", "get", name);
        if (StringUtils.isNotEmpty(opts.getDeployment())) {
            args.add("--deployment");
            args.add(opts.getDeployment());
        }
        execute(args, opts);
    }

    private String execute(List<String> args, NhctlGlobalOptions opts) throws IOException, InterruptedException {
        addGlobalOptions(args, opts);

        String cmd = String.join(" ", args.toArray(new String[]{}));
        System.out.println("Execute command: " + cmd);

        Process process = new ProcessBuilder(args).redirectErrorStream(true).start();
        String output = CharStreams.toString(new InputStreamReader(
                process.getInputStream(), Charsets.UTF_8));
        if (process.waitFor() != 0) {
            throw new RuntimeException(output);
        }

        return output;
    }

    private void addGlobalOptions(List<String> args, NhctlGlobalOptions opts) {
        if (opts.isDebug()) {
            args.add("--debug");
        }
        if (StringUtils.isNotEmpty(opts.getKubeconfig())) {
            args.add("--kubeconfig");
            args.add(opts.getKubeconfig());
        }
    }
}
