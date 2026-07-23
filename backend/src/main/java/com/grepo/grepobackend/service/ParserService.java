package com.grepo.grepobackend.service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.grepo.grepobackend.model.CodeElement;
import com.grepo.grepobackend.model.ElementType;
import com.grepo.grepobackend.model.GitRepository;
import com.grepo.grepobackend.model.SourceFile;
import com.grepo.grepobackend.repository.CodeElementRepository;
import com.grepo.grepobackend.repository.SourceFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParserService {

    private final JavaParser javaParser;
    private final SourceFileRepository sourceFileRepository;
    private final CodeElementRepository codeElementRepository;

    public ParserService(SourceFileRepository sourceFileRepository,
                         CodeElementRepository codeElementRepository) {
        ParserConfiguration config = new ParserConfiguration();
        config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
        this.javaParser = new JavaParser(config);
        this.sourceFileRepository = sourceFileRepository;
        this.codeElementRepository = codeElementRepository;
    }


    public List<File> findJavaFiles(File rootDir){
        List<File> javaFiles = new ArrayList<>();
        collectJavaFiles(rootDir, javaFiles);
        return javaFiles;
    }

    private void collectJavaFiles(File dir, List<File> result){
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                collectJavaFiles(file, result);
            } else if (file.getName().endsWith(".java")) {
                result.add(file);
            }
        }
    }

    public void printParsedFile(File javaFile) {
        try {
            CompilationUnit cu = javaParser.parse(javaFile).getResult()
                    .orElseThrow(() -> new RuntimeException("Parsing returned no result"));

            cu.findAll(ClassOrInterfaceDeclaration.class).forEach(c ->
                    System.out.println("CLASS/INTERFACE: " + c.getNameAsString())
            );

            cu.findAll(MethodDeclaration.class).forEach(m ->
                    System.out.println("METHOD: " + m.getDeclarationAsString())
            );

        } catch (Exception e) {
            System.out.println("Failed to parse " + javaFile.getName() + ": " + e.getMessage());
        }
    }


    public List<CodeElement> parseAndSave(File javaFile, GitRepository repo, File repoRootDir) {
        List<CodeElement> savedElements = new ArrayList<>();

        try {
            CompilationUnit cu = javaParser.parse(javaFile).getResult()
                    .orElseThrow(() -> new RuntimeException("Parsing returned no result"));

            String relativePath = repoRootDir.toPath().relativize(javaFile.toPath()).toString();
            String packageName = cu.getPackageDeclaration()
                    .map(p -> p.getNameAsString())
                    .orElse("");

            SourceFile sourceFile = new SourceFile();
            sourceFile.setRepository(repo);
            sourceFile.setFilePath(relativePath);
            sourceFile.setPackageName(packageName);
            sourceFile = sourceFileRepository.save(sourceFile);

            SourceFile finalSourceFile = sourceFile;

            cu.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> {
                CodeElement el = new CodeElement();
                el.setSourceFile(finalSourceFile);
                el.setType(c.isInterface() ? ElementType.INTERFACE : ElementType.CLASS);
                el.setName(c.getNameAsString());
                el.setSignature(c.getNameAsString());
                el.setStartLine(c.getBegin().map(p -> p.line).orElse(0));
                el.setEndLine(c.getEnd().map(p -> p.line).orElse(0));
                savedElements.add(codeElementRepository.save(el));
            });

            cu.findAll(MethodDeclaration.class).forEach(m -> {
                CodeElement el = new CodeElement();
                el.setSourceFile(finalSourceFile);
                el.setType(ElementType.METHOD);
                el.setName(m.getNameAsString());
                el.setSignature(m.getDeclarationAsString());
                el.setStartLine(m.getBegin().map(p -> p.line).orElse(0));
                el.setEndLine(m.getEnd().map(p -> p.line).orElse(0));
                savedElements.add(codeElementRepository.save(el));
            });

            cu.findAll(AnnotationDeclaration.class).forEach(a -> {
                CodeElement el = new CodeElement();
                el.setSourceFile(finalSourceFile);
                el.setType(ElementType.ANNOTATION);
                el.setName(a.getNameAsString());
                el.setSignature(a.getNameAsString());
                el.setStartLine(a.getBegin().map(p -> p.line).orElse(0));
                el.setEndLine(a.getEnd().map(p -> p.line).orElse(0));
                savedElements.add(codeElementRepository.save(el));
            });


        } catch (Exception e) {
            System.out.println("Failed to parse " + javaFile.getName() + ": " + e.getMessage());
        }
        return savedElements;
    }

}