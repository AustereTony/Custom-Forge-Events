package ru.austeretony.events.coremod;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class CustomEventsClassTransformer implements IClassTransformer {
	
	public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
    	
    	switch (name) {
    	
    	    //Демонстрация трансформеров.
			case "ru.austeretony.events.test.TestClass":
			
				LOGGER.info("<TestClass> transformation attempt...");
			
				return patchTestClass(basicClass);   
    	
    	    //Трансформация Minecraft.class  
    		case "bib":
    			
        		LOGGER.info("Obfuscated <Minecraft> class transformation attempt...");

    			return patchMinecraft(basicClass, true);
    	
			case "net.minecraft.client.Minecraft":
				
	    		LOGGER.info("Debfuscated <Minecraft> class transformation attempt...");
			
	    		return patchMinecraft(basicClass, false);	    		
	        	
	    	//Трансформация Slot.class 
	    	case "agr":
	    			
	        	LOGGER.info("Obfuscated <Slot> class transformation attempt...");

	    		return patchSlot(basicClass, true);
    			
    		case "net.minecraft.inventory.Slot":
    			
        		LOGGER.info("Debfuscated <Slot> class transformation attempt...");
    			
        		return patchSlot(basicClass, false);     	 
    	}
    	
        return basicClass;
    }
    
    //Трансформация TestClass
    public byte[] patchTestClass(byte[] bytes) {
    	
    	/*
    	 * Опкоды всех операций и используемые объекты для создания инструкций с данным опкодом указаны в классе Opcodes.
    	 */
    	
	    ClassNode classNode = new ClassNode();//Новый узел для класса.
        ClassReader classReader = new ClassReader(bytes);//Ридер для байтов трансформируемого класса.
        classReader.accept(classNode, 0);//Добавление ClassNode в ридер для внесения изменений.
        
	 	String targetMethodName = "addTo";//Имя трансформируемого метода.
	 	        
	 	//Начало перебора методов класса.
		for (MethodNode methodNode : classNode.methods) {
			
			//Если имя текушего метода соответствует имени целевого и его дискриптору, работаем с ним.
			if (methodNode.name.equals(targetMethodName) && methodNode.desc.equals("(I)V")) {
								
                AbstractInsnNode currentNode = null;//Ссылка на текущий узел.
                
                //Получение итератора для узлов метода, все узлы приводятся к их суперклассу AbstractInsnNode.
                Iterator<AbstractInsnNode> iteratorNode = methodNode.instructions.iterator();
               
                //Вам тут делать нечего, если вы не можете в циклы...
                while (iteratorNode.hasNext()) {
                	
                    currentNode = iteratorNode.next();
                                        
                    //При нахождении нужного узла работаем с ним.
                    if (currentNode.getOpcode() == Opcodes.INVOKEVIRTUAL) {//узел вызова метода log().
                    	             
                    	//Создание нового списка инструкций для метода.
                    	InsnList nodesList = new InsnList();
                    	
                    	//Добавление первой инструкции: загрузка ссылки на объект класса (локальная переменная this, индекс 0 (всегда)) для получения доступа к полю someInt. Требует создание узла VarInsnNode.
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    	
                    	//Добавление второй инструкции: загрузка (получение) поля someInt из класса TestClass с дескриптором I (int). Требуется создание узла FieldInsnNode.
                    	nodesList.add(new FieldInsnNode(Opcodes.GETFIELD, "ru/austeretony/events/test/TestClass", "someInt", "I"));
                    	
                    	//Добавление третьей инструкции: загрузка локальной переменной типа int под индексом 1 (значение, переданное методу как аргумент). Требует создание узла VarInsnNode.
                    	nodesList.add(new VarInsnNode(Opcodes.ILOAD, 1));
                    	
                    	//Добавление четвёртой инструкции: вызов статического метода с указанным именем и дескриптором из указанного класса (создание нового узла MethodInsnNode для метода), 
                    	//последний флаг определяет является ли класс, из которого вызывается метод, интерфейсом.
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/austeretony/events/test/TestInjections", "showValues", "(II)V", false));
                    	
                    	//Добавление пятой инструкции: досрочный выход из метода через return для предотвращения выполнения оставшейся его части (вызова метода log()). Требуется новый узел InsnNode.
                    	nodesList.add(new InsnNode(Opcodes.RETURN));
                    	
                    	//Добавление списка новых инструкций перед загрузкой ссылки на объект класса для вызова log().
                    	//Так как текущий узел это вызов метода, то вставку требуется произвести перед предыдущим узлом.
                        methodNode.instructions.insertBefore(currentNode.getPrevious(), nodesList); 
                        
                    	break;//Выход из цикла перебора узлов метода.
                    }
                }
                
                break;//Выход из цикла перебора методов.
			}
		}
	 	
	    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);//Создание райтера.	    
	    classNode.accept(writer);//Запись изменёного класса.
	            
	    //Возвращение записанного класса в байтовом представлении для дальнейшей работы.
        return writer.toByteArray();	
    }
    
	//Трансформация класса Minecraft
	public byte[] patchMinecraft(byte[] bytes, boolean obfuscated) {
		        
	    ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
	 	String targetMethodName = obfuscated ? "aD" : "runTickKeyboard";//В зависимости от среды может быть обфускация имён.
	 	
        int bipushCount = 0;
        
        boolean iconstModified = false;
        
        LOGGER.info("Class name: " + classNode.name);

        LOGGER.info("Injection started!");   
        
		for (MethodNode methodNode : classNode.methods) {
			
			//Поиск runTickKeyboard()
			if (methodNode.name.equals(targetMethodName) && methodNode.desc.equals("()V")) {
				
            	LOGGER.info("Method <runTickKeyboard()> found!");
				
                AbstractInsnNode currentNode = null;
                
                Iterator<AbstractInsnNode> iteratorNode = methodNode.instructions.iterator();
               
                while (iteratorNode.hasNext()) {
                	
                    currentNode = iteratorNode.next();
                    
                    /*Поиск ветки:
                     * 
                    * else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
                    * 
                    * 		this.actionKeyF3 = true;
                    *       this.debugCrashKeyPressTime = getSystemTime();
                    *       
                    * }
                    */
                    if (!iconstModified && currentNode.getOpcode() == Opcodes.ICONST_1) {
                    	
                        LOGGER.info("Method <runTickKeyboard()>: ICONST_1 node found"); 
                            
                        InsnList nodesList = new InsnList();
                        	
                        //Загрузка возвращаемого onDebugMenuOpen() начения.
                        nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/austeretony/events/coremod/CustomEventsFactory", "onDebugMenuOpen", "()Z", false));
                        //Добавление ветки if. Второй аргумент конструктора JumpInsnNode требует метку (label), к которой будет относится условие. 
                        //В данном случае условие будет вложено - ищем узел внешнего условия и приводя его к JumpInsnNode получаем его метку (смещение на 4 узла назад).
                        nodesList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) currentNode.getPrevious().getPrevious().getPrevious().getPrevious()).label));
                        	
                        methodNode.instructions.insertBefore(currentNode.getPrevious(), nodesList); 
                        
                        iconstModified = true;//Исключаем модификацию последующих узлов с таким же опкодом (ICONST_1).

                        LOGGER.info("Method <runTickKeyboard()>: <onDebugMenuOpen()> injected before previous node!");                              
                    }  
                    
                    /*
                     * Поиск ветки:
                     * 
                     * if (i == 59) {
                     * 
                     * 		this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                     * }
                     */
                    if (currentNode.getOpcode() == Opcodes.BIPUSH) {
                    	
                    	if (bipushCount < 7) {//Загрузка константы (BIPUSH) в седьмой раз происходит именно для этой ветки.
                    		
                    		bipushCount++;
                    	}
                    	
                    	if (bipushCount == 7) {
                        	
                            LOGGER.info("Method <runTickKeyboard()>: seventh BIPUSH node found"); 
                            
                        	InsnList nodesList = new InsnList();
                        	
                        	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/austeretony/events/coremod/CustomEventsFactory", "onGUIHide", "()Z", false));
                        	nodesList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) currentNode.getNext()).label));
                        	
                            methodNode.instructions.insert(currentNode.getNext(), nodesList); 

                            LOGGER.info("Method <runTickKeyboard()>: <onGUIHide()> injected after next node!"); 
                            
                            //Устанавливаем новое искомое имя метода для главого цикла т.к. с текущим методом закончили.
                            targetMethodName = obfuscated ? "c" : "processKeyF3";
                                
                            break;//Выход из цикла по узлам runTickKeyboard().                               
                        } 
                    }                                                                       
                }
			}
            
			//Поиск processKeyF3()
            if (methodNode.name.equals(targetMethodName) && methodNode.desc.equals("(I)Z")) {
            	
            	LOGGER.info("Method <processKeyF3()> found!");
                
                AbstractInsnNode currentNode = null;
                
                Iterator<AbstractInsnNode> iteratorNode = methodNode.instructions.iterator();
               
                while (iteratorNode.hasNext()) {
                	
                    currentNode = iteratorNode.next();
                                              
                    //Работаем в самом начале метода перед загрузкой первой локальной переменной (значение аргумена типа int)
                    if (currentNode.getOpcode() == Opcodes.ILOAD) {
                    	
                    	LOGGER.info("Method <processKeyF3()>: ILOAD node found!");
                    	
                    	InsnList nodesList = new InsnList();
                    	
                    	//Уже делали подобное.
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/austeretony/events/coremod/CustomEventsFactory", "onDebugMenuOpen", "()Z", false));
                    	nodesList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) currentNode.getNext().getNext()).label));
                    	
                    	//При отмене события мы перейдём в нашу ветку if
                    	//И что бы выйти из метода нужно создать точку выхода return.
                    	//Метод должен возвращать логическое значение - генерируем return true (именно).
                    	
                    	//Загрузка константы false (интересно - приставка I (int), а возвращаемое 
                    	//значение типа boolean - значения типа int и boolean используют одинаковые опкоды (в данном случае константы - true - ICONST_1, false - ICONST_0).
                    	nodesList.add(new InsnNode(Opcodes.ICONST_1));
                    	nodesList.add(new InsnNode(Opcodes.IRETURN));//return для возвращения значения.
                    	
                        methodNode.instructions.insertBefore(currentNode, nodesList); 
                        
                        LOGGER.info("Method <processKeyF3()>: <onDebugMenuOpen()> injected before current node!"); 
                        
                        break;
                    }
                }
                
                break;
            }
		}
		
	    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);	    
	    classNode.accept(writer);
	    
	    LOGGER.info("Injection ended!");
        
        return writer.toByteArray();				
	}
    
	//Трансформация класса Slot
	public byte[] patchSlot(byte[] bytes, boolean obfuscated) {
        
	    ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
	 	String 
	 	targetMethodName = obfuscated ? "a" : "onTake",
	 	entityPlayerClassName = obfuscated ? "aed" : "net/minecraft/entity/player/EntityPlayer",
	 	itemStackClassName = obfuscated ? "aip" : "net/minecraft/item/ItemStack",
	 	slotClassName = obfuscated ? "agr" : "net/minecraft/inventory/Slot";
        
        boolean 
        onTakePreInjected = false,
        putStackPreInjected = false,
        putStackPostInjected = false;
        
        LOGGER.info("Class name: " + classNode.name);
            
        LOGGER.info("Injection started!");
        
		for (MethodNode methodNode : classNode.methods) {
            
			//Поик onTake(). Учтите наличие обфускации!
            if (methodNode.name.equals(targetMethodName) && methodNode.desc.equals("(L" + entityPlayerClassName + ";L" + itemStackClassName + ";)L" + itemStackClassName + ";")) {
            	
            	LOGGER.info("Method <onTake()> found!");
                
                AbstractInsnNode currentNode = null;
                
                Iterator<AbstractInsnNode> iteratorNode = methodNode.instructions.iterator();

                while (iteratorNode.hasNext()) {
                	
                    currentNode = iteratorNode.next();
                    
                    //Поиск первой загрузки ссылки на объект класса (this) - ALOAD
                    if (!onTakePreInjected && currentNode.getOpcode() == Opcodes.ALOAD) {
                    	
                    	LOGGER.info("Method <onTake()>: ALOAD node found!");
                    	
                    	InsnList nodesList = new InsnList();//Новый список.
                    	
                    	//Загрузка локальных перемменных в порядке онных в сигнатуре вызываемого за ними метода.
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    	//Вставка вызова CustomEventsFactory#onTakePre()
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/austeretony/events/coremod/CustomEventsFactory", "onTakePre", "(L" + slotClassName + ";L" + entityPlayerClassName + ";L" + itemStackClassName + ";)V", false));
                    	                       
                    	//Вставка инструкций в начало метода - перед загрузкой локальной переменной ссылки на объект класса (this)
                        methodNode.instructions.insertBefore(currentNode, nodesList); 
                        
                        //Предотвращаем вставку этих же инструкций перед вторым узлом ALOAD.
                        onTakePreInjected = true;

                        LOGGER.info("Method <onTake()>: <onTakePre> injected before current node!");  
                    }    
                    
                    if (currentNode.getOpcode() == Opcodes.ARETURN) {
                    	
                    	LOGGER.info("Method <onTake()>: ARETURN node found!");
                    	
                    	InsnList nodesList = new InsnList();
                    	
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/austeretony/events/coremod/CustomEventsFactory", "onTakePost", "(L" + slotClassName + ";L" + entityPlayerClassName + ";L" + itemStackClassName + ";)V", false));
                    	
                    	//Вставка инструкций перед вторым ALOAD (текущий узел - ARETURN, получаем предыдущий (ALOAD с помощью getPrevious()). 
                        methodNode.instructions.insertBefore(currentNode.getPrevious(), nodesList); 

                        LOGGER.info("Method <onTake()>: <onTakePost> injected before current node!"); 
                		
                		break;//Выход из цикла по узлам метода onTake().
                    }
                }
                
                targetMethodName = obfuscated ? "d" : "putStack";//Меняем искомое имя метода на putStack() для его поиска в главном цикле.
            }
            
            //Поиск putStack()
            if (methodNode.name.equals(targetMethodName) && methodNode.desc.equals("(L" + itemStackClassName + ";)V")) {
            	
            	LOGGER.info("Method <putStack()> found!");
                
                AbstractInsnNode currentNode = null;
                
                Iterator<AbstractInsnNode> iteratorNode = methodNode.instructions.iterator();

                while (iteratorNode.hasNext()) {
                	
                    currentNode = iteratorNode.next();
                    
                    if (!putStackPreInjected && currentNode.getOpcode() == Opcodes.ALOAD) {
                    	
                    	LOGGER.info("Method <putStack()>: ALOAD node found!");
                    	
                    	InsnList nodesList = new InsnList();
                    	
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/austeretony/events/coremod/CustomEventsFactory", "putStackPre", "(L" + slotClassName + ";L" + itemStackClassName + ";)V", false));
                    	
                    	//Вставка инструкций перед текущим узлом (загрузкой this) - в самое начало метода.
                        methodNode.instructions.insertBefore(currentNode, nodesList);
                    	                        
                        LOGGER.info("Method <putStack()>: <putStackPre> injected before current node!");  
                		
                		putStackPreInjected = true;
                    }    
                    
                    if (currentNode.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                    	
                    	LOGGER.info("Method <putStack()>: INVOKEVIRTUAL node found!");    
                    	
                    	InsnList nodesList = new InsnList();
                    	
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/austeretony/events/coremod/CustomEventsFactory", "putStackPost", "(L" + slotClassName + ";L" + itemStackClassName + ";)V", false));                 
                    	
                    	//Вставка новых инструкций после текущего узла (после вызова onSlotChanged()).
                        methodNode.instructions.insert(currentNode, nodesList);               
                        
                        LOGGER.info("Method <putStack()>: <putStackPost> injected after current node!"); 
                		
                		break;
                    }
                }
                
                break;
            }
        }
                
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);        
        classNode.accept(writer);
        
        LOGGER.info("Injection ended!");
        
        return writer.toByteArray();
    }
}
